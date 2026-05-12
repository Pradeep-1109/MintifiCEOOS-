package com.mintifi.ceoos.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00ca\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010S\u001a\u00020N2\u0006\u0010T\u001a\u00020\u001eJ\u000e\u0010U\u001a\u00020N2\u0006\u0010V\u001a\u00020\'J\u0016\u0010W\u001a\u00020X2\u0006\u0010Y\u001a\u00020\t2\u0006\u0010Z\u001a\u00020\tJ\b\u0010[\u001a\u00020\u000bH\u0002J\u000e\u0010\\\u001a\u00020N2\u0006\u0010]\u001a\u00020^J\u0006\u0010_\u001a\u00020XJ\u000e\u0010`\u001a\u00020N2\u0006\u0010T\u001a\u00020\u001eJ\u000e\u0010a\u001a\u00020N2\u0006\u0010b\u001a\u00020!J\u000e\u0010c\u001a\u00020N2\u0006\u0010d\u001a\u00020$J\u000e\u0010e\u001a\u00020N2\u0006\u0010V\u001a\u00020\'J\u0006\u0010f\u001a\u00020NJ\u000e\u0010g\u001a\u00020N2\u0006\u0010h\u001a\u00020\u0011J\u0006\u0010i\u001a\u00020NJ\u0016\u0010j\u001a\u00020N2\u0006\u0010k\u001a\u00020l2\u0006\u0010]\u001a\u00020^J\u0006\u0010m\u001a\u00020XJ\u0016\u0010n\u001a\u00020X2\u0006\u0010o\u001a\u00020p2\u0006\u0010Z\u001a\u00020\tJ\u0006\u0010q\u001a\u00020XJ\u0006\u0010r\u001a\u00020XJ\u000e\u0010s\u001a\u00020N2\u0006\u0010t\u001a\u00020\tJ\u000e\u0010u\u001a\u00020X2\u0006\u0010v\u001a\u00020\u0016J\u0006\u0010w\u001a\u00020XJ\u000e\u0010x\u001a\u00020X2\u0006\u0010Z\u001a\u00020\tJ\u000e\u0010y\u001a\u00020X2\u0006\u0010z\u001a\u00020{J\u0016\u0010|\u001a\u00020N2\u0006\u0010h\u001a\u00020\u00112\u0006\u0010}\u001a\u00020\u000bJ\u000e\u0010~\u001a\u00020X2\u0006\u0010b\u001a\u00020\u007fJ\u0018\u0010\u0080\u0001\u001a\u00020N2\u0006\u0010h\u001a\u00020\u00112\u0007\u0010\u0081\u0001\u001a\u00020\u000bJ\u000f\u0010\u0082\u0001\u001a\u00020N2\u0006\u0010T\u001a\u00020\u001eJ\u000f\u0010\u0083\u0001\u001a\u00020N2\u0006\u0010d\u001a\u00020$J\u0018\u0010\u0084\u0001\u001a\u00020N2\u0006\u0010h\u001a\u00020\u00112\u0007\u0010\u0085\u0001\u001a\u00020\u007fR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0010\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00110\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0012\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0017\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00110\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u001d\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001e0\u001d0\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001bR\u001d\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020!0\u001d0\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001bR\u001d\u0010#\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020$0\u001d0\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001bR\u001d\u0010&\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\'0\u001d0\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001bR\u0017\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00070\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001bR\u000e\u0010+\u001a\u00020,X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010-\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020.0\u001d0\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010\u001bR\u000e\u00100\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0019\u00101\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010\u001bR\u0017\u00103\u001a\b\u0012\u0004\u0012\u0002040\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u0010\u001bR\u000e\u00106\u001a\u000207X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u00108\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020$0\u001d0\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u0010\u001bR\u0017\u0010:\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b:\u0010\u001bR\u0017\u0010;\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u0010\u001bR\u0017\u0010<\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b<\u0010\u001bR\u0017\u0010=\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010\u001bR\u0017\u0010>\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b>\u0010\u001bR\u0019\u0010?\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00110\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b@\u0010\u001bR\u0019\u0010A\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\bB\u0010\u001bR\u001d\u0010C\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020D0\u001d0\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\bE\u0010\u001bR\u0017\u0010F\u001a\b\u0012\u0004\u0012\u00020\u00110\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\bG\u0010\u001bR\u0017\u0010H\u001a\b\u0012\u0004\u0012\u00020\u00140\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\bI\u0010\u001bR\u000e\u0010J\u001a\u00020\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010K\u001a\b\u0012\u0004\u0012\u00020\u00160\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\bL\u0010\u001bR\u0010\u0010M\u001a\u0004\u0018\u00010NX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001d\u0010O\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\'0\u001d0\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\bP\u0010\u001bR\u0019\u0010Q\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\bR\u0010\u001b\u00a8\u0006\u0086\u0001"}, d2 = {"Lcom/mintifi/ceoos/viewmodel/MainViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "app", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_amplitude", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_dailyBriefing", "", "_isAnalyzing", "", "_isCopilotLoading", "_isOnline", "_isPaused", "_isRecording", "_lastSessionId", "", "_lastSummary", "_recordingDur", "", "_taskFilter", "Lcom/mintifi/ceoos/data/model/TaskFilter;", "_uiMessage", "activeRemindersCount", "Lkotlinx/coroutines/flow/StateFlow;", "getActiveRemindersCount", "()Lkotlinx/coroutines/flow/StateFlow;", "allReminders", "", "Lcom/mintifi/ceoos/data/model/Reminder;", "getAllReminders", "allSessions", "Lcom/mintifi/ceoos/data/model/Session;", "getAllSessions", "allTasks", "Lcom/mintifi/ceoos/data/model/Task;", "getAllTasks", "allTodos", "Lcom/mintifi/ceoos/data/model/TodoItem;", "getAllTodos", "amplitude", "getAmplitude", "api", "Lcom/mintifi/ceoos/data/service/ApiService;", "chatMessages", "Lcom/mintifi/ceoos/data/model/ChatMessage;", "getChatMessages", "currentSessionId", "dailyBriefing", "getDailyBriefing", "dashboardStats", "Lcom/mintifi/ceoos/data/model/DashboardStats;", "getDashboardStats", "db", "Lcom/mintifi/ceoos/data/database/AppDatabase;", "filteredTasks", "getFilteredTasks", "isAnalyzing", "isCopilotLoading", "isOnline", "isPaused", "isRecording", "lastSessionId", "getLastSessionId", "lastSummary", "getLastSummary", "offlineQueue", "Lcom/mintifi/ceoos/data/model/OfflineQueueItem;", "getOfflineQueue", "offlineQueueCount", "getOfflineQueueCount", "recordingDur", "getRecordingDur", "recordingStartTime", "taskFilter", "getTaskFilter", "timerJob", "Lkotlinx/coroutines/Job;", "todayTodos", "getTodayTodos", "uiMessage", "getUiMessage", "addReminder", "r", "addTodo", "item", "analyzeTextOnly", "", "text", "contextNote", "checkOnline", "clearCompleted", "listType", "Lcom/mintifi/ceoos/data/model/TodoListType;", "clearFilters", "deleteReminder", "deleteSession", "s", "deleteTask", "task", "deleteTodo", "generateBriefing", "hideSession", "id", "loadPresetReminders", "loadTemplate", "template", "Lcom/mintifi/ceoos/data/model/WorkTemplate;", "pauseRecording", "processAudioFile", "file", "Ljava/io/File;", "processOfflineQueue", "resumeRecording", "sendCopilotMessage", "msg", "setFilter", "f", "startRecording", "stopAndProcess", "togglePriority", "p", "Lcom/mintifi/ceoos/data/model/Priority;", "toggleReminder", "active", "toggleStatus", "Lcom/mintifi/ceoos/data/model/TaskStatus;", "toggleTodo", "done", "updateReminder", "updateTask", "updateTaskStatus", "status", "app_debug"})
public final class MainViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.mintifi.ceoos.data.database.AppDatabase db = null;
    @org.jetbrains.annotations.NotNull()
    private final com.mintifi.ceoos.data.service.ApiService api = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.mintifi.ceoos.data.model.Session>> allSessions = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.mintifi.ceoos.data.model.Task>> allTasks = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.mintifi.ceoos.data.model.Reminder>> allReminders = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.mintifi.ceoos.data.model.TodoItem>> allTodos = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.mintifi.ceoos.data.model.ChatMessage>> chatMessages = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.mintifi.ceoos.data.model.OfflineQueueItem>> offlineQueue = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> offlineQueueCount = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.mintifi.ceoos.data.model.DashboardStats> dashboardStats = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.mintifi.ceoos.data.model.TodoItem>> todayTodos = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> activeRemindersCount = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.mintifi.ceoos.data.model.TaskFilter> _taskFilter = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.mintifi.ceoos.data.model.TaskFilter> taskFilter = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.mintifi.ceoos.data.model.Task>> filteredTasks = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isRecording = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isPaused = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Long> _recordingDur = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Float> _amplitude = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isAnalyzing = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _uiMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _dailyBriefing = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isCopilotLoading = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _lastSummary = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Integer> _lastSessionId = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isOnline = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isRecording = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isPaused = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Long> recordingDur = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Float> amplitude = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isAnalyzing = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> uiMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> dailyBriefing = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isCopilotLoading = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> lastSummary = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> lastSessionId = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isOnline = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job timerJob;
    private int currentSessionId = 0;
    private long recordingStartTime = 0L;
    
    public MainViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application app) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.mintifi.ceoos.data.model.Session>> getAllSessions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.mintifi.ceoos.data.model.Task>> getAllTasks() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.mintifi.ceoos.data.model.Reminder>> getAllReminders() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.mintifi.ceoos.data.model.TodoItem>> getAllTodos() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.mintifi.ceoos.data.model.ChatMessage>> getChatMessages() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.mintifi.ceoos.data.model.OfflineQueueItem>> getOfflineQueue() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getOfflineQueueCount() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.mintifi.ceoos.data.model.DashboardStats> getDashboardStats() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.mintifi.ceoos.data.model.TodoItem>> getTodayTodos() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getActiveRemindersCount() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.mintifi.ceoos.data.model.TaskFilter> getTaskFilter() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.mintifi.ceoos.data.model.Task>> getFilteredTasks() {
        return null;
    }
    
    public final void setFilter(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.TaskFilter f) {
    }
    
    public final void togglePriority(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.Priority p) {
    }
    
    public final void toggleStatus(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.TaskStatus s) {
    }
    
    public final void clearFilters() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isRecording() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isPaused() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Long> getRecordingDur() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Float> getAmplitude() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isAnalyzing() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getUiMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getDailyBriefing() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isCopilotLoading() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getLastSummary() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getLastSessionId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isOnline() {
        return null;
    }
    
    private final boolean checkOnline() {
        return false;
    }
    
    public final void startRecording() {
    }
    
    public final void pauseRecording() {
    }
    
    public final void resumeRecording() {
    }
    
    public final void stopAndProcess(@org.jetbrains.annotations.NotNull()
    java.lang.String contextNote) {
    }
    
    public final void processOfflineQueue() {
    }
    
    public final void processAudioFile(@org.jetbrains.annotations.NotNull()
    java.io.File file, @org.jetbrains.annotations.NotNull()
    java.lang.String contextNote) {
    }
    
    public final void analyzeTextOnly(@org.jetbrains.annotations.NotNull()
    java.lang.String text, @org.jetbrains.annotations.NotNull()
    java.lang.String contextNote) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job updateTaskStatus(int id, @org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.TaskStatus status) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job updateTask(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.Task task) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job deleteTask(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.Task task) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job deleteSession(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.Session s) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job hideSession(int id) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job addReminder(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.Reminder r) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job updateReminder(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.Reminder r) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job deleteReminder(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.Reminder r) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job toggleReminder(int id, boolean active) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job loadPresetReminders() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job addTodo(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.TodoItem item) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job deleteTodo(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.TodoItem item) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job toggleTodo(int id, boolean done) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job clearCompleted(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.TodoListType listType) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job loadTemplate(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.WorkTemplate template, @org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.TodoListType listType) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job sendCopilotMessage(@org.jetbrains.annotations.NotNull()
    java.lang.String msg) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job generateBriefing() {
        return null;
    }
}