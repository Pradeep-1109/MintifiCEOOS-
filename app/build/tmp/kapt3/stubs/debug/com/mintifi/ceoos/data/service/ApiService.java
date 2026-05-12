package com.mintifi.ceoos.data.service;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J&\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000bH\u0086@\u00a2\u0006\u0002\u0010\rJ\b\u0010\u000e\u001a\u00020\u000bH\u0002J\u0018\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u000bH\u0002J2\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u000b2\u0018\u0010\u0014\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b0\u00150\b2\u0006\u0010\u0011\u001a\u00020\u000bH\u0002J\u0010\u0010\u0016\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u000bH\u0002J*\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u000b2\u0018\u0010\u0014\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b0\u00150\bH\u0002J\u0018\u0010\u0018\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u000bH\u0002J\u0018\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u000bH\u0002J2\u0010\u001a\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u000b2\u0018\u0010\u0014\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b0\u00150\b2\u0006\u0010\u0011\u001a\u00020\u000bH\u0002J\u0018\u0010\u001b\u001a\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\u001c2\u0006\u0010\u0011\u001a\u00020\u000bH\u0002J0\u0010\u001d\u001a\u00020\u000b2\u0018\u0010\u0014\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b0\u00150\b2\u0006\u0010\u001e\u001a\u00020\u000bH\u0086@\u00a2\u0006\u0002\u0010\u001fJ\b\u0010 \u001a\u00020\u000bH\u0002J\b\u0010!\u001a\u00020\u000bH\u0002J\u001c\u0010\"\u001a\u00020\u000b2\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0086@\u00a2\u0006\u0002\u0010$J\u0016\u0010%\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000bH\u0086@\u00a2\u0006\u0002\u0010&J\b\u0010\'\u001a\u00020\u000bH\u0002J\u0016\u0010(\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010)\u001a\u00020\u000bH\u0002J\u0010\u0010*\u001a\n ,*\u0004\u0018\u00010+0+H\u0002J\u0016\u0010-\u001a\u00020\u000b2\u0006\u0010.\u001a\u00020/H\u0086@\u00a2\u0006\u0002\u00100J\u0018\u00101\u001a\u00020\u000b2\u0006\u0010.\u001a\u00020/2\u0006\u0010\u0011\u001a\u00020\u000bH\u0002J\u0018\u00102\u001a\u00020\u000b2\u0006\u0010.\u001a\u00020/2\u0006\u0010\u0011\u001a\u00020\u000bH\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00063"}, d2 = {"Lcom/mintifi/ceoos/data/service/ApiService;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "client", "Lokhttp3/OkHttpClient;", "analyzeTranscript", "", "Lcom/mintifi/ceoos/data/model/Task;", "transcript", "", "ctx", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "anthropicKey", "callAnthropic", "prompt", "key", "callAnthropicHistory", "system", "messages", "Lkotlin/Pair;", "callBestAI", "callBestAIWithHistory", "callGemini", "callGroq", "callGroqHistory", "callGroqMessages", "Lorg/json/JSONArray;", "chatWithCopilot", "taskContext", "(Ljava/util/List;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deepgramKey", "geminiKey", "generateBriefing", "tasks", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "generateSummary", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "groqKey", "parseTasksFromResponse", "response", "prefs", "Landroid/content/SharedPreferences;", "kotlin.jvm.PlatformType", "transcribeAudio", "file", "Ljava/io/File;", "(Ljava/io/File;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "transcribeDeepgram", "transcribeGroq", "app_debug"})
public final class ApiService {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final okhttp3.OkHttpClient client = null;
    
    public ApiService(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    private final android.content.SharedPreferences prefs() {
        return null;
    }
    
    private final java.lang.String geminiKey() {
        return null;
    }
    
    private final java.lang.String groqKey() {
        return null;
    }
    
    private final java.lang.String anthropicKey() {
        return null;
    }
    
    private final java.lang.String deepgramKey() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object transcribeAudio(@org.jetbrains.annotations.NotNull()
    java.io.File file, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    private final java.lang.String transcribeGroq(java.io.File file, java.lang.String key) {
        return null;
    }
    
    private final java.lang.String transcribeDeepgram(java.io.File file, java.lang.String key) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object generateSummary(@org.jetbrains.annotations.NotNull()
    java.lang.String transcript, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object analyzeTranscript(@org.jetbrains.annotations.NotNull()
    java.lang.String transcript, @org.jetbrains.annotations.NotNull()
    java.lang.String ctx, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.mintifi.ceoos.data.model.Task>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object generateBriefing(@org.jetbrains.annotations.NotNull()
    java.util.List<com.mintifi.ceoos.data.model.Task> tasks, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object chatWithCopilot(@org.jetbrains.annotations.NotNull()
    java.util.List<kotlin.Pair<java.lang.String, java.lang.String>> messages, @org.jetbrains.annotations.NotNull()
    java.lang.String taskContext, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    private final java.lang.String callBestAI(java.lang.String prompt) {
        return null;
    }
    
    private final java.lang.String callBestAIWithHistory(java.lang.String system, java.util.List<kotlin.Pair<java.lang.String, java.lang.String>> messages) {
        return null;
    }
    
    private final java.lang.String callGemini(java.lang.String prompt, java.lang.String key) {
        return null;
    }
    
    private final java.lang.String callGroq(java.lang.String prompt, java.lang.String key) {
        return null;
    }
    
    private final java.lang.String callGroqHistory(java.lang.String system, java.util.List<kotlin.Pair<java.lang.String, java.lang.String>> messages, java.lang.String key) {
        return null;
    }
    
    private final java.lang.String callGroqMessages(org.json.JSONArray messages, java.lang.String key) {
        return null;
    }
    
    private final java.lang.String callAnthropic(java.lang.String prompt, java.lang.String key) {
        return null;
    }
    
    private final java.lang.String callAnthropicHistory(java.lang.String system, java.util.List<kotlin.Pair<java.lang.String, java.lang.String>> messages, java.lang.String key) {
        return null;
    }
    
    private final java.util.List<com.mintifi.ceoos.data.model.Task> parseTasksFromResponse(java.lang.String response) {
        return null;
    }
}