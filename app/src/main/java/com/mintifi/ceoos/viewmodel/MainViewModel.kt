package com.mintifi.ceoos.viewmodel
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mintifi.ceoos.data.database.AppDatabase
import com.mintifi.ceoos.data.model.*
import com.mintifi.ceoos.data.service.ApiService
import com.mintifi.ceoos.data.service.RecordingService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.File

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val db = AppDatabase.getInstance(app)
    private val api = ApiService(app)

    val allSessions  = db.sessionDao().getAll().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val allTasks     = db.taskDao().getAll().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val allReminders = db.reminderDao().getAll().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val allTodos     = db.todoDao().getAll().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val chatMessages = db.chatDao().getAll().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val dashboardStats = allTasks.map { tasks ->
        DashboardStats(total=tasks.size, p0Count=tasks.count{it.priority==Priority.P0}, p1Count=tasks.count{it.priority==Priority.P1}, p2Count=tasks.count{it.priority==Priority.P2}, p3Count=tasks.count{it.priority==Priority.P3}, completedCount=tasks.count{it.status==TaskStatus.COMPLETED})
    }.stateIn(viewModelScope, SharingStarted.Lazily, DashboardStats())

    val todayTodos = allTodos.map { it.filter { t -> t.listType == TodoListType.TODAY } }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val activeRemindersCount = allReminders.map { it.count { r -> r.isActive } }.stateIn(viewModelScope, SharingStarted.Lazily, 0)

    private val _taskFilter = MutableStateFlow(TaskFilter())
    val taskFilter = _taskFilter.asStateFlow()

    val filteredTasks = combine(allTasks, _taskFilter) { tasks, filter ->
        tasks.filter { task ->
            (filter.query.isBlank() || task.title.contains(filter.query, true) || task.detail.contains(filter.query, true) || task.projectGroup.contains(filter.query, true)) &&
            (filter.priorities.isEmpty() || task.priority in filter.priorities) &&
            (filter.statuses.isEmpty() || task.status in filter.statuses) &&
            (filter.sessionId == null || task.sessionId == filter.sessionId)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setFilter(f: TaskFilter) { _taskFilter.value = f }
    fun togglePriority(p: Priority) { val c=_taskFilter.value; _taskFilter.value=c.copy(priorities=if(p in c.priorities)c.priorities-p else c.priorities+p) }
    fun toggleStatus(s: TaskStatus) { val c=_taskFilter.value; _taskFilter.value=c.copy(statuses=if(s in c.statuses)c.statuses-s else c.statuses+s) }
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

    private var timerJob: Job? = null
    private var currentSessionId = 0
    private var recordingStartTime = 0L

    fun startRecording() {
        val ctx = getApplication<Application>()
        ctx.startForegroundService(Intent(ctx, RecordingService::class.java).also { it.action = RecordingService.ACTION_START })
        _isRecording.value = true; _isPaused.value = false; _recordingDur.value = 0L; _lastSummary.value = null
        recordingStartTime = System.currentTimeMillis()
        timerJob = viewModelScope.launch { while (_isRecording.value) { delay(100); if(!_isPaused.value) _recordingDur.value+=100; _amplitude.value=(0.3f+Math.random().toFloat()*0.7f) } }
        viewModelScope.launch { currentSessionId = db.sessionDao().insert(Session(title="CEO Call", status=SessionStatus.RECORDING)).toInt() }
    }

    fun pauseRecording() { getApplication<Application>().startService(Intent(getApplication(), RecordingService::class.java).also{it.action=RecordingService.ACTION_PAUSE}); _isPaused.value=true }
    fun resumeRecording() { getApplication<Application>().startService(Intent(getApplication(), RecordingService::class.java).also{it.action=RecordingService.ACTION_RESUME}); _isPaused.value=false }

    fun stopAndProcess(contextNote: String) {
        getApplication<Application>().startService(Intent(getApplication(), RecordingService::class.java).also{it.action=RecordingService.ACTION_STOP})
        _isRecording.value=false; _isPaused.value=false; timerJob?.cancel()
        RecordingService.currentFilePath?.let { processAudioFile(File(it), contextNote) }
    }

    fun processAudioFile(file: File, contextNote: String) {
        viewModelScope.launch {
            _isAnalyzing.value = true
            _uiMessage.value = "Transcribing (Hindi+English supported)..."
            val durationMs = System.currentTimeMillis() - recordingStartTime
            runCatching {
                val transcript = api.transcribeAudio(file)
                _uiMessage.value = "Generating summary..."
                val summary = api.generateSummary(transcript)
                _lastSummary.value = summary
                _uiMessage.value = "Extracting tasks..."
                val tasks = api.analyzeTranscript(transcript, contextNote)
                val sid = if (currentSessionId > 0) currentSessionId else
                    db.sessionDao().insert(Session(title="CEO Call", status=SessionStatus.PROCESSING)).toInt()
                db.taskDao().insertAll(tasks.map { it.copy(sessionId = sid) })
                db.sessionDao().update(Session(id=sid, title="CEO Call " + java.text.SimpleDateFormat("dd MMM HH:mm", java.util.Locale.getDefault()).format(java.util.Date()), status=SessionStatus.DONE, taskCount=tasks.size, transcript=transcript, summary=summary, durationMs=durationMs))
                _lastSessionId.value = sid
                _uiMessage.value = "Done! Found " + tasks.size + " tasks from " + tasks.map{it.projectGroup}.toSet().size + " projects."
            }.onFailure { e -> _uiMessage.value = "Error: " + e.message }
            _isAnalyzing.value = false; currentSessionId = 0; delay(6000); _uiMessage.value = null
        }
    }

    fun analyzeTextOnly(text: String, contextNote: String) {
        viewModelScope.launch {
            _isAnalyzing.value = true; _uiMessage.value = "Analyzing text (Hindi+English)..."
            runCatching {
                val summary = api.generateSummary(text)
                _lastSummary.value = summary
                val tasks = api.analyzeTranscript(text, contextNote)
                val sid = db.sessionDao().insert(Session(title="Manual Entry " + java.text.SimpleDateFormat("dd MMM HH:mm", java.util.Locale.getDefault()).format(java.util.Date()), status=SessionStatus.DONE, transcript=text, summary=summary, taskCount=tasks.size)).toInt()
                db.taskDao().insertAll(tasks.map { it.copy(sessionId = sid) })
                _lastSessionId.value = sid
                _uiMessage.value = "Done! Found " + tasks.size + " tasks."
            }.onFailure { e -> _uiMessage.value = "Error: " + e.message }
            _isAnalyzing.value = false; delay(4000); _uiMessage.value = null
        }
    }

    fun updateTaskStatus(id: Int, status: TaskStatus) = viewModelScope.launch { db.taskDao().updateStatus(id, status) }
    fun deleteSession(s: Session) = viewModelScope.launch { db.sessionDao().delete(s) }
    fun hideSession(id: Int) = viewModelScope.launch { db.sessionDao().hide(id) }

    fun addReminder(r: Reminder)    = viewModelScope.launch { db.reminderDao().insert(r) }
    fun updateReminder(r: Reminder) = viewModelScope.launch { db.reminderDao().update(r) }
    fun deleteReminder(r: Reminder) = viewModelScope.launch { db.reminderDao().delete(r) }
    fun toggleReminder(id: Int, active: Boolean) = viewModelScope.launch { db.reminderDao().setActive(id, active) }

    fun loadPresetReminders() = viewModelScope.launch {
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
    }

    fun addTodo(item: TodoItem)    = viewModelScope.launch { db.todoDao().insert(item) }
    fun deleteTodo(item: TodoItem) = viewModelScope.launch { db.todoDao().delete(item) }
    fun toggleTodo(id: Int, done: Boolean) = viewModelScope.launch { db.todoDao().setCompleted(id, done) }
    fun clearCompleted(listType: TodoListType) = viewModelScope.launch { db.todoDao().clearCompleted(listType) }
    fun loadTemplate(template: WorkTemplate, listType: TodoListType) = viewModelScope.launch {
        db.todoDao().insertAll(template.items.map { TodoItem(title=it, listType=listType) })
    }

    fun sendCopilotMessage(msg: String) = viewModelScope.launch {
        db.chatDao().insert(ChatMessage(role="user", content=msg))
        _isCopilotLoading.value = true
        runCatching {
            val history = chatMessages.value.takeLast(20).map { m -> m.role to m.content }
            val taskCtx = allTasks.value.take(30).joinToString("
") { t -> "[" + t.priority.label + "][" + t.status.label + "] " + t.title }
            db.chatDao().insert(ChatMessage(role="assistant", content=api.chatWithCopilot(history, taskCtx)))
        }.onFailure { e -> db.chatDao().insert(ChatMessage(role="assistant", content="Error: " + e.message)) }
        _isCopilotLoading.value = false
    }

    fun generateBriefing() = viewModelScope.launch {
        _dailyBriefing.value = null
        runCatching { _dailyBriefing.value = api.generateBriefing(allTasks.value.filter{it.status!=TaskStatus.COMPLETED}.take(20)) }
            .onFailure { e -> _dailyBriefing.value = "Error: " + e.message }
    }
}
