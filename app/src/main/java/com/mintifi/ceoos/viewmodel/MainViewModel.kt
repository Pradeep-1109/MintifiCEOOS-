package com.mintifi.ceoos.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mintifi.ceoos.data.database.AppDatabase
import com.mintifi.ceoos.data.model.*
import com.mintifi.ceoos.data.service.ApiService
import com.mintifi.ceoos.data.service.RecordingService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val db  = AppDatabase.getInstance(app)
    private val api = ApiService(app)

    val allSessions       = db.sessionDao().getAll().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val allTasks          = db.taskDao().getAll().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val allReminders      = db.reminderDao().getAll().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val allTodos          = db.todoDao().getAll().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val chatMessages      = db.chatDao().getAll().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val offlineQueue      = db.offlineQueueDao().getPending().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val offlineQueueCount = db.offlineQueueDao().getPendingCount().stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val dashboardStats = allTasks.map { tasks ->
        DashboardStats(
            total          = tasks.size,
            p0Count        = tasks.count { it.priority == Priority.P0 },
            p1Count        = tasks.count { it.priority == Priority.P1 },
            p2Count        = tasks.count { it.priority == Priority.P2 },
            p3Count        = tasks.count { it.priority == Priority.P3 },
            completedCount = tasks.count { it.status == TaskStatus.COMPLETED }
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, DashboardStats())

    val todayTodos = allTodos.map { list ->
        list.filter { it.listType == TodoListType.TODAY }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val activeRemindersCount = allReminders.map { list ->
        list.count { it.isActive }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0)

    private val _taskFilter = MutableStateFlow(TaskFilter())
    val taskFilter = _taskFilter.asStateFlow()

    val filteredTasks = combine(allTasks, _taskFilter) { tasks, filter ->
        tasks.filter { task ->
            (filter.query.isBlank() ||
                task.title.contains(filter.query, ignoreCase = true) ||
                task.detail.contains(filter.query, ignoreCase = true) ||
                task.projectGroup.contains(filter.query, ignoreCase = true)) &&
            (filter.priorities.isEmpty() || task.priority in filter.priorities) &&
            (filter.statuses.isEmpty()   || task.status   in filter.statuses) &&
            (filter.sessionId == null    || task.sessionId == filter.sessionId)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setFilter(f: TaskFilter) { _taskFilter.value = f }
    fun togglePriority(p: Priority) {
        val c = _taskFilter.value
        _taskFilter.value = c.copy(priorities = if (p in c.priorities) c.priorities - p else c.priorities + p)
    }
    fun toggleStatus(s: TaskStatus) {
        val c = _taskFilter.value
        _taskFilter.value = c.copy(statuses = if (s in c.statuses) c.statuses - s else c.statuses + s)
    }
    fun clearFilters() { _taskFilter.value = TaskFilter() }

    private val _isRecording      = MutableStateFlow(false)
    private val _isPaused         = MutableStateFlow(false)
    private val _recordingDur     = MutableStateFlow(0L)
    private val _amplitude        = MutableStateFlow(0f)
    private val _isAnalyzing      = MutableStateFlow(false)
    private val _uiMessage        = MutableStateFlow<String?>(null)
    private val _dailyBriefing    = MutableStateFlow<String?>(null)
    private val _isCopilotLoading = MutableStateFlow(false)
    private val _lastSummary      = MutableStateFlow<String?>(null)
    private val _lastSessionId    = MutableStateFlow<Int?>(null)
    private val _isOnline         = MutableStateFlow(true)

    val isRecording      = _isRecording.asStateFlow()
    val isPaused         = _isPaused.asStateFlow()
    val recordingDur     = _recordingDur.asStateFlow()
    val amplitude        = _amplitude.asStateFlow()
    val isAnalyzing      = _isAnalyzing.asStateFlow()
    val uiMessage        = _uiMessage.asStateFlow()
    val dailyBriefing    = _dailyBriefing.asStateFlow()
    val isCopilotLoading = _isCopilotLoading.asStateFlow()
    val lastSummary      = _lastSummary.asStateFlow()
    val lastSessionId    = _lastSessionId.asStateFlow()
    val isOnline         = _isOnline.asStateFlow()

    private var timerJob: Job? = null
    private var currentSessionId = 0
    private var recordingStartTime = 0L

    private fun checkOnline(): Boolean {
        return try {
            val cm = getApplication<Application>()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = cm.activeNetwork ?: return false
            val caps = cm.getNetworkCapabilities(network) ?: return false
            val online = caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            _isOnline.value = online
            online
        } catch (e: Exception) {
            Timber.e(e, "checkOnline error")
            true // assume online if check fails
        }
    }

    fun startRecording() {
        try {
            val ctx = getApplication<Application>()
            val intent = Intent(ctx, RecordingService::class.java)
            intent.action = RecordingService.ACTION_START
            ctx.startForegroundService(intent)
            _isRecording.value  = true
            _isPaused.value     = false
            _recordingDur.value = 0L
            _lastSummary.value  = null
            recordingStartTime  = System.currentTimeMillis()
            timerJob = viewModelScope.launch {
                while (_isRecording.value) {
                    delay(100)
                    if (!_isPaused.value) _recordingDur.value += 100
                    _amplitude.value = (0.3f + Math.random().toFloat() * 0.7f)
                }
            }
            viewModelScope.launch {
                try {
                    currentSessionId = db.sessionDao().insert(
                        Session(title = "CEO Call", status = SessionStatus.RECORDING)
                    ).toInt()
                } catch (e: Exception) {
                    Timber.e(e, "Failed to insert session")
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "startRecording error")
            _uiMessage.value = "Failed to start recording: " + e.message
        }
    }

    fun pauseRecording() {
        try {
            val intent = Intent(getApplication(), RecordingService::class.java)
            intent.action = RecordingService.ACTION_PAUSE
            getApplication<Application>().startService(intent)
            _isPaused.value = true
        } catch (e: Exception) {
            Timber.e(e, "pauseRecording error")
        }
    }

    fun resumeRecording() {
        try {
            val intent = Intent(getApplication(), RecordingService::class.java)
            intent.action = RecordingService.ACTION_RESUME
            getApplication<Application>().startService(intent)
            _isPaused.value = false
        } catch (e: Exception) {
            Timber.e(e, "resumeRecording error")
        }
    }

    fun stopAndProcess(contextNote: String) {
        // 1. Update UI state immediately so screen doesn't freeze
        _isRecording.value = false
        _isPaused.value    = false
        timerJob?.cancel()

        // 2. Capture path BEFORE stopping service
        val path = RecordingService.currentFilePath

        // 3. Stop the service
        try {
            val intent = Intent(getApplication(), RecordingService::class.java)
            intent.action = RecordingService.ACTION_STOP
            getApplication<Application>().startService(intent)
        } catch (e: Exception) {
            Timber.e(e, "Failed to stop recording service")
        }

        // 4. Wait for file to finalize, then process
        viewModelScope.launch {
            if (path == null) {
                _uiMessage.value = "No recording found. Please try again."
                delay(3000)
                _uiMessage.value = null
                return@launch
            }

            // Wait for MediaRecorder to flush and close the file
            delay(1200)

            val file = File(path)
            if (!file.exists()) {
                _uiMessage.value = "Recording file not found. Please try again."
                delay(3000)
                _uiMessage.value = null
                return@launch
            }
            if (file.length() < 1000) {
                _uiMessage.value = "Recording too short or empty. Please try again."
                delay(3000)
                _uiMessage.value = null
                return@launch
            }

            Timber.d("Recording file ready: ${file.length()} bytes at $path")

            if (checkOnline()) {
                processAudioFile(file, contextNote)
            } else {
                try {
                    db.offlineQueueDao().insert(
                        OfflineQueueItem(audioPath = path, contextNote = contextNote)
                    )
                    _uiMessage.value = "No internet. Recording saved to offline queue."
                } catch (e: Exception) {
                    _uiMessage.value = "Error saving to queue: " + e.message
                }
                delay(4000)
                _uiMessage.value = null
            }
        }
    }

    fun processOfflineQueue() {
        viewModelScope.launch {
            if (!checkOnline()) {
                _uiMessage.value = "Still offline. Connect to internet first."
                delay(3000); _uiMessage.value = null
                return@launch
            }
            val queue = offlineQueue.value
            if (queue.isEmpty()) {
                _uiMessage.value = "Offline queue is empty."
                delay(2000); _uiMessage.value = null
                return@launch
            }
            _uiMessage.value = "Processing " + queue.size + " queued recordings..."
            queue.forEach { item ->
                try {
                    db.offlineQueueDao().markProcessing(item.id)
                    val file = File(item.audioPath)
                    if (file.exists() && file.length() > 1000) {
                        processAudioFile(file, item.contextNote)
                    }
                    db.offlineQueueDao().markDone(item.id)
                } catch (e: Exception) {
                    Timber.e(e, "Error processing queue item ${item.id}")
                }
            }
            db.offlineQueueDao().clearDone()
        }
    }

    fun processAudioFile(file: File, contextNote: String) {
        viewModelScope.launch {
            _isAnalyzing.value = true
            _uiMessage.value   = "Transcribing audio (Hindi+English)..."
            val durationMs     = System.currentTimeMillis() - recordingStartTime
            val dateFmt        = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault())
            try {
                val transcript   = api.transcribeAudio(file)
                if (transcript.isBlank()) {
                    _uiMessage.value = "Transcription returned empty. Check Groq API key in Settings."
                    delay(4000); _uiMessage.value = null
                    _isAnalyzing.value = false
                    return@launch
                }
                _uiMessage.value = "Generating meeting summary..."
                val summary = try { api.generateSummary(transcript) } catch (e: Exception) { "" }
                _lastSummary.value = if (summary.isNotBlank()) summary else null

                _uiMessage.value = "Extracting tasks..."
                val tasks = try { api.analyzeTranscript(transcript, contextNote) } catch (e: Exception) { emptyList() }

                val sid = if (currentSessionId > 0) currentSessionId else {
                    db.sessionDao().insert(
                        Session(title = "CEO Call", status = SessionStatus.PROCESSING)
                    ).toInt()
                }

                if (tasks.isNotEmpty()) {
                    db.taskDao().insertAll(tasks.map { it.copy(sessionId = sid) })
                }

                val projectCount = tasks.map { it.projectGroup }.toSet().size
                db.sessionDao().update(
                    Session(
                        id         = sid,
                        title      = "CEO Call " + dateFmt.format(Date()),
                        status     = SessionStatus.DONE,
                        taskCount  = tasks.size,
                        transcript = transcript,
                        summary    = summary,
                        durationMs = durationMs
                    )
                )
                _lastSessionId.value = sid

                if (tasks.isEmpty()) {
                    _uiMessage.value = "Done! No action items found. Check if AI key is set in Settings."
                } else {
                    _uiMessage.value = "Done! Found " + tasks.size + " tasks from " + projectCount + " projects."
                }
            } catch (e: Exception) {
                Timber.e(e, "processAudioFile error")
                _uiMessage.value = "Error: " + (e.message ?: "Unknown error. Check API keys in Settings.")
            }
            _isAnalyzing.value = false
            currentSessionId   = 0
            delay(6000)
            _uiMessage.value   = null
        }
    }

    fun analyzeTextOnly(text: String, contextNote: String) {
        viewModelScope.launch {
            if (!checkOnline()) {
                _uiMessage.value = "No internet. Please connect and try again."
                delay(3000); _uiMessage.value = null
                return@launch
            }
            _isAnalyzing.value = true
            _uiMessage.value   = "Analyzing text (Hindi+English)..."
            val dateFmt = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault())
            try {
                val summary = try { api.generateSummary(text) } catch (e: Exception) { "" }
                if (summary.isNotBlank()) _lastSummary.value = summary

                val tasks = api.analyzeTranscript(text, contextNote)
                val sid = db.sessionDao().insert(
                    Session(
                        title      = "Manual Entry " + dateFmt.format(Date()),
                        status     = SessionStatus.DONE,
                        transcript = text,
                        summary    = summary,
                        taskCount  = tasks.size
                    )
                ).toInt()
                if (tasks.isNotEmpty()) {
                    db.taskDao().insertAll(tasks.map { it.copy(sessionId = sid) })
                }
                _lastSessionId.value = sid
                _uiMessage.value = "Done! Found " + tasks.size + " tasks."
            } catch (e: Exception) {
                Timber.e(e, "analyzeTextOnly error")
                _uiMessage.value = "Error: " + (e.message ?: "Check API keys in Settings.")
            }
            _isAnalyzing.value = false
            delay(4000)
            _uiMessage.value = null
        }
    }

    fun updateTaskStatus(id: Int, status: TaskStatus) = viewModelScope.launch {
        try { db.taskDao().updateStatus(id, status) } catch (e: Exception) { Timber.e(e) }
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        try { db.taskDao().update(task) } catch (e: Exception) { Timber.e(e) }
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        try { db.taskDao().delete(task) } catch (e: Exception) { Timber.e(e) }
    }

    fun deleteSession(s: Session) = viewModelScope.launch {
        try { db.sessionDao().delete(s) } catch (e: Exception) { Timber.e(e) }
    }

    fun hideSession(id: Int) = viewModelScope.launch {
        try { db.sessionDao().hide(id) } catch (e: Exception) { Timber.e(e) }
    }

    fun addReminder(r: Reminder)    = viewModelScope.launch { try { db.reminderDao().insert(r) } catch (e: Exception) { Timber.e(e) } }
    fun updateReminder(r: Reminder) = viewModelScope.launch { try { db.reminderDao().update(r) } catch (e: Exception) { Timber.e(e) } }
    fun deleteReminder(r: Reminder) = viewModelScope.launch { try { db.reminderDao().delete(r) } catch (e: Exception) { Timber.e(e) } }
    fun toggleReminder(id: Int, active: Boolean) = viewModelScope.launch {
        try { db.reminderDao().setActive(id, active) } catch (e: Exception) { Timber.e(e) }
    }

    fun loadPresetReminders() = viewModelScope.launch {
        try {
            db.reminderDao().insertAll(listOf(
                Reminder(title = "CEO Call Prep",          category = ReminderCategory.CEO_PREP, timeHour = 8,  timeMinute = 30, repeatType = RepeatType.WEEKDAYS),
                Reminder(title = "DDR Daily Review",       category = ReminderCategory.WORK,     timeHour = 10, timeMinute = 0,  repeatType = RepeatType.WEEKDAYS),
                Reminder(title = "Collections Escalation", category = ReminderCategory.WORK,     timeHour = 12, timeMinute = 0,  repeatType = RepeatType.WEEKDAYS),
                Reminder(title = "LAP Portfolio Monitor",  category = ReminderCategory.WORK,     timeHour = 14, timeMinute = 0,  repeatType = RepeatType.WEEKLY),
                Reminder(title = "MBA Study Session",      category = ReminderCategory.MBA,      timeHour = 20, timeMinute = 0,  repeatType = RepeatType.DAILY),
                Reminder(title = "EOD Wrap-up",            category = ReminderCategory.WORK,     timeHour = 18, timeMinute = 30, repeatType = RepeatType.WEEKDAYS),
                Reminder(title = "Weekly Review",          category = ReminderCategory.CEO_PREP, timeHour = 16, timeMinute = 0,  repeatType = RepeatType.WEEKLY),
                Reminder(title = "Morning Standup",        category = ReminderCategory.WORK,     timeHour = 9,  timeMinute = 0,  repeatType = RepeatType.WEEKDAYS)
            ))
        } catch (e: Exception) { Timber.e(e) }
    }

    fun addTodo(item: TodoItem)    = viewModelScope.launch { try { db.todoDao().insert(item) } catch (e: Exception) { Timber.e(e) } }
    fun deleteTodo(item: TodoItem) = viewModelScope.launch { try { db.todoDao().delete(item) } catch (e: Exception) { Timber.e(e) } }
    fun toggleTodo(id: Int, done: Boolean) = viewModelScope.launch {
        try { db.todoDao().setCompleted(id, done) } catch (e: Exception) { Timber.e(e) }
    }
    fun clearCompleted(listType: TodoListType) = viewModelScope.launch {
        try { db.todoDao().clearCompleted(listType) } catch (e: Exception) { Timber.e(e) }
    }
    fun loadTemplate(template: WorkTemplate, listType: TodoListType) = viewModelScope.launch {
        try { db.todoDao().insertAll(template.items.map { TodoItem(title = it, listType = listType) }) } catch (e: Exception) { Timber.e(e) }
    }

    fun sendCopilotMessage(msg: String) = viewModelScope.launch {
        try {
            db.chatDao().insert(ChatMessage(role = "user", content = msg))
            _isCopilotLoading.value = true
            val history = chatMessages.value.takeLast(20).map { m -> m.role to m.content }
            val sb = StringBuilder()
            allTasks.value.take(30).forEach { t ->
                sb.append("[").append(t.priority.label).append("][")
                    .append(t.status.label).append("] ").append(t.title).append(" ")
            }
            val response = api.chatWithCopilot(history, sb.toString())
            db.chatDao().insert(ChatMessage(role = "assistant", content = response))
        } catch (e: Exception) {
            Timber.e(e, "copilot error")
            try { db.chatDao().insert(ChatMessage(role = "assistant", content = "Error: " + e.message)) } catch (_: Exception) {}
        }
        _isCopilotLoading.value = false
    }

    fun generateBriefing() = viewModelScope.launch {
        _dailyBriefing.value = null
        try {
            val tasks = allTasks.value.filter { it.status != TaskStatus.COMPLETED }.take(20)
            _dailyBriefing.value = api.generateBriefing(tasks)
        } catch (e: Exception) {
            Timber.e(e, "generateBriefing error")
            _dailyBriefing.value = "Error: " + e.message
        }
    }
}
