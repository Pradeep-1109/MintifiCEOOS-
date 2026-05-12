package com.mintifi.ceoos.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
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
    private var mediaPlayer: MediaPlayer? = null

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

    val todayTodos = allTodos.map { it.filter { t -> t.listType == TodoListType.TODAY } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val activeRemindersCount = allReminders.map { it.count { r -> r.isActive } }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    // ── Filter ────────────────────────────────────────────────────────────────

    private val _taskFilter = MutableStateFlow(TaskFilter())
    val taskFilter = _taskFilter.asStateFlow()

    val filteredTasks = combine(allTasks, _taskFilter) { tasks, filter ->
        tasks.filter { task ->
            (filter.query.isBlank() ||
                task.title.contains(filter.query, ignoreCase = true) ||
                task.detail.contains(filter.query, ignoreCase = true) ||
                task.projectGroup.contains(filter.query, ignoreCase = true)) &&
            (filter.priorities.isEmpty() || task.priority in filter.priorities) &&
            (filter.statuses.isEmpty()   || task.status   in filter.statuses)
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

    // ── State ─────────────────────────────────────────────────────────────────

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
    private val _isPlayingAudio   = MutableStateFlow(false)
    private val _playingSessionId = MutableStateFlow<Int?>(null)
    private val _selectedSpeaker  = MutableStateFlow<String?>(null)

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
    val isPlayingAudio   = _isPlayingAudio.asStateFlow()
    val playingSessionId = _playingSessionId.asStateFlow()
    val selectedSpeaker  = _selectedSpeaker.asStateFlow()

    private var timerJob: Job? = null
    private var currentSessionId = 0
    private var recordingStartTime = 0L

    // ── Speaker Selection ─────────────────────────────────────────────────────

    fun selectSpeaker(speaker: String?) { _selectedSpeaker.value = speaker }

    fun buildContextFromSpeaker(customNote: String): String {
        val speaker = _selectedSpeaker.value
        return if (speaker != null) {
            "Meeting with: $speaker. " + customNote
        } else customNote
    }

    // ── Connectivity ──────────────────────────────────────────────────────────

    private fun checkOnline(): Boolean {
        return try {
            val cm = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val caps = cm.getNetworkCapabilities(cm.activeNetwork ?: return false) ?: return false
            val online = caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            _isOnline.value = online
            online
        } catch (e: Exception) { true }
    }

    // ── Recording ─────────────────────────────────────────────────────────────

    fun startRecording() {
        try {
            val ctx = getApplication<Application>()
            ctx.startForegroundService(
                Intent(ctx, RecordingService::class.java).also { it.action = RecordingService.ACTION_START }
            )
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
                // FIX: reset amplitude to 0 when loop ends (recording stopped)
                _amplitude.value = 0f
            }
            viewModelScope.launch {
                try {
                    val speakerNote = _selectedSpeaker.value?.let { "Discussion with $it." } ?: ""
                    currentSessionId = db.sessionDao().insert(
                        Session(title = "CEO Call", status = SessionStatus.RECORDING)
                    ).toInt()
                } catch (e: Exception) { Timber.e(e) }
            }
        } catch (e: Exception) {
            Timber.e(e, "startRecording error")
            _uiMessage.value = "Failed to start: " + e.message
        }
    }

    fun pauseRecording() {
        try {
            getApplication<Application>().startService(
                Intent(getApplication(), RecordingService::class.java).also { it.action = RecordingService.ACTION_PAUSE }
            )
            _isPaused.value = true
        } catch (e: Exception) { Timber.e(e) }
    }

    fun resumeRecording() {
        try {
            getApplication<Application>().startService(
                Intent(getApplication(), RecordingService::class.java).also { it.action = RecordingService.ACTION_RESUME }
            )
            _isPaused.value = false
        } catch (e: Exception) { Timber.e(e) }
    }

    fun stopAndProcess(contextNote: String) {
        // 1. Stop UI animation immediately
        _isRecording.value = false
        _isPaused.value    = false
        _amplitude.value   = 0f  // FIX: explicitly stop animation
        timerJob?.cancel()

        val path = RecordingService.currentFilePath

        // 2. Stop service
        try {
            getApplication<Application>().startService(
                Intent(getApplication(), RecordingService::class.java).also { it.action = RecordingService.ACTION_STOP }
            )
        } catch (e: Exception) { Timber.e(e) }

        // 3. Process file after delay
        viewModelScope.launch {
            if (path == null) {
                _uiMessage.value = "No recording found. Please try again."
                delay(3000); _uiMessage.value = null
                return@launch
            }

            _uiMessage.value = "Finalizing recording..."
            delay(1500) // wait for MediaRecorder to flush

            val file = File(path)
            if (!file.exists() || file.length() < 1000L) {
                _uiMessage.value = "Recording too short or empty. Please record for at least 3 seconds."
                delay(4000); _uiMessage.value = null
                return@launch
            }

            val fullContext = buildContextFromSpeaker(contextNote)

            if (checkOnline()) {
                processAudioFile(file, fullContext)
            } else {
                try {
                    db.offlineQueueDao().insert(OfflineQueueItem(audioPath = path, contextNote = fullContext))
                    _uiMessage.value = "No internet. Recording saved to offline queue."
                } catch (e: Exception) { _uiMessage.value = "Error saving queue: " + e.message }
                delay(4000); _uiMessage.value = null
            }
        }
    }

    // ── Process Audio — with fallback retry ───────────────────────────────────

    fun processAudioFile(file: File, contextNote: String, isRetry: Boolean = false) {
        viewModelScope.launch {
            _isAnalyzing.value = true
            _uiMessage.value   = if (isRetry) "Retrying transcription..." else "Transcribing (Hindi+English)..."
            val durationMs     = System.currentTimeMillis() - recordingStartTime
            val dateFmt        = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault())
            try {
                val transcript = api.transcribeAudio(file)
                if (transcript.isBlank()) {
                    _uiMessage.value = "Transcription empty. Check Groq API key in Settings."
                    _isAnalyzing.value = false
                    delay(4000); _uiMessage.value = null
                    return@launch
                }

                _uiMessage.value = "Generating summary..."
                val summary = try { api.generateSummary(transcript) } catch (e: Exception) { "" }
                if (summary.isNotBlank()) _lastSummary.value = summary

                _uiMessage.value = "Extracting tasks..."
                val tasks = try { api.analyzeTranscript(transcript, contextNote) } catch (e: Exception) { emptyList() }

                val sid = if (currentSessionId > 0) currentSessionId else {
                    db.sessionDao().insert(Session(title = "CEO Call", status = SessionStatus.PROCESSING)).toInt()
                }

                // FALLBACK: if 0 tasks and not already a retry, retry once
                if (tasks.isEmpty() && !isRetry) {
                    _uiMessage.value = "No tasks found. Retrying with enhanced extraction..."
                    delay(1000)
                    val retryTasks = try { api.analyzeTranscript(transcript, contextNote + " Extract ALL action items mentioned even briefly.") } catch (e: Exception) { emptyList() }
                    if (retryTasks.isNotEmpty()) {
                        db.taskDao().insertAll(retryTasks.map { it.copy(sessionId = sid) })
                        autoAddToTodo(retryTasks)
                        _uiMessage.value = "Done! Found " + retryTasks.size + " tasks (retry)."
                    } else {
                        _uiMessage.value = "No action items detected. Transcript saved. Check Tasks tab."
                    }
                } else if (tasks.isNotEmpty()) {
                    db.taskDao().insertAll(tasks.map { it.copy(sessionId = sid) })
                    autoAddToTodo(tasks)
                    val projectCount = tasks.map { it.projectGroup }.toSet().size
                    _uiMessage.value = "Done! Found " + tasks.size + " tasks from " + projectCount + " projects."
                }

                db.sessionDao().update(
                    Session(
                        id         = sid,
                        title      = "CEO Call " + dateFmt.format(Date()),
                        status     = SessionStatus.DONE,
                        taskCount  = tasks.size,
                        transcript = transcript,
                        summary    = summary,
                        durationMs = durationMs,
                        audioPath  = file.absolutePath
                    )
                )
                _lastSessionId.value = sid
            } catch (e: Exception) {
                Timber.e(e, "processAudioFile error")
                _uiMessage.value = "Error: " + (e.message ?: "Check API keys in Settings.")
            }
            _isAnalyzing.value = false
            currentSessionId   = 0
            delay(6000)
            _uiMessage.value   = null
        }
    }

    // ── Auto add P0/P1 to Today's To-Do ──────────────────────────────────────

    private suspend fun autoAddToTodo(tasks: List<Task>) {
        try {
            val highPriority = tasks.filter { it.priority == Priority.P0 || it.priority == Priority.P1 }
            if (highPriority.isNotEmpty()) {
                db.todoDao().insertAll(highPriority.map { task ->
                    TodoItem(
                        title    = task.title,
                        listType = TodoListType.TODAY,
                        priority = TodoPriority.HIGH
                    )
                })
            }
        } catch (e: Exception) { Timber.e(e, "autoAddToTodo error") }
    }

    // ── Add task to todo manually ─────────────────────────────────────────────

    fun addTaskToTodo(task: Task) = viewModelScope.launch {
        try {
            db.todoDao().insert(TodoItem(title = task.title, listType = TodoListType.TODAY, priority = TodoPriority.HIGH))
            _uiMessage.value = "Added to Today's To-Do"
            delay(2000); _uiMessage.value = null
        } catch (e: Exception) { Timber.e(e) }
    }

    // ── Set reminder from task ────────────────────────────────────────────────

    fun addReminderFromTask(task: Task) = viewModelScope.launch {
        try {
            db.reminderDao().insert(
                Reminder(
                    title      = task.title,
                    category   = when (task.category) {
                        Category.DDR        -> ReminderCategory.WORK
                        Category.MBA        -> ReminderCategory.MBA
                        Category.CEO_ASK    -> ReminderCategory.CEO_PREP
                        else                -> ReminderCategory.WORK
                    },
                    repeatType = RepeatType.ONCE,
                    timeHour   = 9,
                    timeMinute = 0
                )
            )
            _uiMessage.value = "Reminder set for: " + task.title
            delay(2000); _uiMessage.value = null
        } catch (e: Exception) { Timber.e(e) }
    }

    // ── Regenerate tasks from saved audio ─────────────────────────────────────

    fun regenerateTasksForSession(session: Session) = viewModelScope.launch {
        val audioPath = session.audioPath
        if (audioPath.isBlank()) {
            _uiMessage.value = "No audio file saved for this session."
            delay(3000); _uiMessage.value = null
            return@launch
        }
        val file = File(audioPath)
        if (!file.exists()) {
            _uiMessage.value = "Audio file not found. It may have been deleted."
            delay(3000); _uiMessage.value = null
            return@launch
        }
        if (!checkOnline()) {
            _uiMessage.value = "No internet. Connect and try again."
            delay(3000); _uiMessage.value = null
            return@launch
        }
        currentSessionId = session.id
        processAudioFile(file, "Regenerating tasks for session: " + session.title)
    }

    // ── Audio Playback ────────────────────────────────────────────────────────

    fun playSessionAudio(session: Session) {
        val path = session.audioPath
        if (path.isBlank() || !File(path).exists()) {
            viewModelScope.launch {
                _uiMessage.value = "Audio file not available for playback."
                delay(3000); _uiMessage.value = null
            }
            return
        }
        try {
            stopAudio()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(path)
                prepare()
                start()
                _isPlayingAudio.value   = true
                _playingSessionId.value = session.id
                setOnCompletionListener {
                    _isPlayingAudio.value   = false
                    _playingSessionId.value = null
                    release()
                    mediaPlayer = null
                }
                setOnErrorListener { _, _, _ ->
                    _isPlayingAudio.value   = false
                    _playingSessionId.value = null
                    false
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "playSessionAudio error")
            _isPlayingAudio.value   = false
            _playingSessionId.value = null
            viewModelScope.launch {
                _uiMessage.value = "Cannot play audio: " + e.message
                delay(3000); _uiMessage.value = null
            }
        }
    }

    fun stopAudio() {
        try { mediaPlayer?.stop() } catch (e: Exception) { Timber.e(e) }
        try { mediaPlayer?.release() } catch (e: Exception) { Timber.e(e) }
        mediaPlayer = null
        _isPlayingAudio.value   = false
        _playingSessionId.value = null
    }

    // ── Text analysis ─────────────────────────────────────────────────────────

    fun analyzeTextOnly(text: String, contextNote: String) {
        viewModelScope.launch {
            if (!checkOnline()) {
                _uiMessage.value = "No internet. Please connect and try again."
                delay(3000); _uiMessage.value = null
                return@launch
            }
            _isAnalyzing.value = true
            _uiMessage.value   = "Analyzing (Hindi+English)..."
            val dateFmt = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault())
            try {
                val summary = try { api.generateSummary(text) } catch (e: Exception) { "" }
                if (summary.isNotBlank()) _lastSummary.value = summary
                val tasks = api.analyzeTranscript(text, contextNote)
                val sid = db.sessionDao().insert(
                    Session(title = "Manual Entry " + dateFmt.format(Date()), status = SessionStatus.DONE,
                        transcript = text, summary = summary, taskCount = tasks.size)
                ).toInt()
                if (tasks.isNotEmpty()) {
                    db.taskDao().insertAll(tasks.map { it.copy(sessionId = sid) })
                    autoAddToTodo(tasks)
                }
                _lastSessionId.value = sid
                _uiMessage.value = "Done! Found " + tasks.size + " tasks."
            } catch (e: Exception) {
                _uiMessage.value = "Error: " + (e.message ?: "Check API keys.")
            }
            _isAnalyzing.value = false
            delay(4000); _uiMessage.value = null
        }
    }

    // ── Offline queue ─────────────────────────────────────────────────────────

    fun processOfflineQueue() {
        viewModelScope.launch {
            if (!checkOnline()) { _uiMessage.value = "Still offline."; delay(2000); _uiMessage.value = null; return@launch }
            val queue = offlineQueue.value
            if (queue.isEmpty()) { _uiMessage.value = "Queue is empty."; delay(2000); _uiMessage.value = null; return@launch }
            _uiMessage.value = "Processing " + queue.size + " queued recordings..."
            queue.forEach { item ->
                try {
                    db.offlineQueueDao().markProcessing(item.id)
                    val file = File(item.audioPath)
                    if (file.exists() && file.length() > 1000) processAudioFile(file, item.contextNote)
                    db.offlineQueueDao().markDone(item.id)
                } catch (e: Exception) { Timber.e(e) }
            }
            db.offlineQueueDao().clearDone()
        }
    }

    // ── Tasks ─────────────────────────────────────────────────────────────────

    fun updateTaskStatus(id: Int, status: TaskStatus) = viewModelScope.launch { try { db.taskDao().updateStatus(id, status) } catch (e: Exception) { Timber.e(e) } }
    fun updateTask(task: Task) = viewModelScope.launch { try { db.taskDao().update(task) } catch (e: Exception) { Timber.e(e) } }
    fun deleteTask(task: Task) = viewModelScope.launch { try { db.taskDao().delete(task) } catch (e: Exception) { Timber.e(e) } }

    // ── Sessions ──────────────────────────────────────────────────────────────

    fun deleteSession(s: Session) = viewModelScope.launch { try { db.sessionDao().delete(s) } catch (e: Exception) { Timber.e(e) } }
    fun hideSession(id: Int)      = viewModelScope.launch { try { db.sessionDao().hide(id) } catch (e: Exception) { Timber.e(e) } }

    // ── Reminders ─────────────────────────────────────────────────────────────

    fun addReminder(r: Reminder)    = viewModelScope.launch { try { db.reminderDao().insert(r) } catch (e: Exception) { Timber.e(e) } }
    fun updateReminder(r: Reminder) = viewModelScope.launch { try { db.reminderDao().update(r) } catch (e: Exception) { Timber.e(e) } }
    fun deleteReminder(r: Reminder) = viewModelScope.launch { try { db.reminderDao().delete(r) } catch (e: Exception) { Timber.e(e) } }
    fun toggleReminder(id: Int, active: Boolean) = viewModelScope.launch { try { db.reminderDao().setActive(id, active) } catch (e: Exception) { Timber.e(e) } }

    fun loadPresetReminders() = viewModelScope.launch {
        try {
            db.reminderDao().insertAll(listOf(
                Reminder(title="CEO Call Prep",          category=ReminderCategory.CEO_PREP, timeHour=8,  timeMinute=30, repeatType=RepeatType.WEEKDAYS),
                Reminder(title="DDR Daily Review",       category=ReminderCategory.WORK,     timeHour=10, timeMinute=0,  repeatType=RepeatType.WEEKDAYS),
                Reminder(title="Collections Escalation", category=ReminderCategory.WORK,     timeHour=12, timeMinute=0,  repeatType=RepeatType.WEEKDAYS),
                Reminder(title="LAP Portfolio Monitor",  category=ReminderCategory.WORK,     timeHour=14, timeMinute=0,  repeatType=RepeatType.WEEKLY),
                Reminder(title="MBA Study Session",      category=ReminderCategory.MBA,      timeHour=20, timeMinute=0,  repeatType=RepeatType.DAILY),
                Reminder(title="EOD Wrap-up",            category=ReminderCategory.WORK,     timeHour=18, timeMinute=30, repeatType=RepeatType.WEEKDAYS),
                Reminder(title="Weekly Review",          category=ReminderCategory.CEO_PREP, timeHour=16, timeMinute=0,  repeatType=RepeatType.WEEKLY),
                Reminder(title="Morning Standup",        category=ReminderCategory.WORK,     timeHour=9,  timeMinute=0,  repeatType=RepeatType.WEEKDAYS)
            ))
        } catch (e: Exception) { Timber.e(e) }
    }

    // ── Todos ─────────────────────────────────────────────────────────────────

    fun addTodo(item: TodoItem)    = viewModelScope.launch { try { db.todoDao().insert(item) } catch (e: Exception) { Timber.e(e) } }
    fun deleteTodo(item: TodoItem) = viewModelScope.launch { try { db.todoDao().delete(item) } catch (e: Exception) { Timber.e(e) } }
    fun toggleTodo(id: Int, done: Boolean) = viewModelScope.launch { try { db.todoDao().setCompleted(id, done) } catch (e: Exception) { Timber.e(e) } }
    fun clearCompleted(listType: TodoListType) = viewModelScope.launch { try { db.todoDao().clearCompleted(listType) } catch (e: Exception) { Timber.e(e) } }
    fun loadTemplate(template: WorkTemplate, listType: TodoListType) = viewModelScope.launch {
        try { db.todoDao().insertAll(template.items.map { TodoItem(title = it, listType = listType) }) } catch (e: Exception) { Timber.e(e) }
    }

    // ── Copilot ───────────────────────────────────────────────────────────────

    fun sendCopilotMessage(msg: String) = viewModelScope.launch {
        try {
            db.chatDao().insert(ChatMessage(role = "user", content = msg))
            _isCopilotLoading.value = true
            val history = chatMessages.value.takeLast(10).map { m -> m.role to m.content }
            val sb = StringBuilder()
            allTasks.value.take(20).forEach { t ->
                sb.append("[").append(t.priority.label).append("] ").append(t.title).append(". ")
            }
            val response = api.chatWithCopilot(history, sb.toString())
            db.chatDao().insert(ChatMessage(role = "assistant", content = response))
        } catch (e: Exception) {
            Timber.e(e)
            try { db.chatDao().insert(ChatMessage(role = "assistant", content = "Error: " + e.message)) } catch (_: Exception) {}
        }
        _isCopilotLoading.value = false
    }

    // ── Briefing ──────────────────────────────────────────────────────────────

    fun generateBriefing() = viewModelScope.launch {
        _dailyBriefing.value = null
        try {
            val tasks = allTasks.value.filter { it.status != TaskStatus.COMPLETED }.take(20)
            _dailyBriefing.value = api.generateBriefing(tasks)
        } catch (e: Exception) { _dailyBriefing.value = "Error: " + e.message }
    }

    override fun onCleared() {
        super.onCleared()
        stopAudio()
    }
}
