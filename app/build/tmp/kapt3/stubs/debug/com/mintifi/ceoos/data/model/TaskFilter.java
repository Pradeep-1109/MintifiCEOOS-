package com.mintifi.ceoos.data.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0010\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B;\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0014\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\u000f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\b0\u0005H\u00c6\u0003J\u0010\u0010\u0017\u001a\u0004\u0018\u00010\nH\u00c6\u0003\u00a2\u0006\u0002\u0010\u0011JD\u0010\u0018\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u00052\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\nH\u00c6\u0001\u00a2\u0006\u0002\u0010\u0019J\u0013\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001d\u001a\u00020\nH\u00d6\u0001J\t\u0010\u001e\u001a\u00020\u0003H\u00d6\u0001R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0015\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0010\u0010\u0011R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\r\u00a8\u0006\u001f"}, d2 = {"Lcom/mintifi/ceoos/data/model/TaskFilter;", "", "query", "", "priorities", "", "Lcom/mintifi/ceoos/data/model/Priority;", "statuses", "Lcom/mintifi/ceoos/data/model/TaskStatus;", "sessionId", "", "(Ljava/lang/String;Ljava/util/Set;Ljava/util/Set;Ljava/lang/Integer;)V", "getPriorities", "()Ljava/util/Set;", "getQuery", "()Ljava/lang/String;", "getSessionId", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getStatuses", "component1", "component2", "component3", "component4", "copy", "(Ljava/lang/String;Ljava/util/Set;Ljava/util/Set;Ljava/lang/Integer;)Lcom/mintifi/ceoos/data/model/TaskFilter;", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class TaskFilter {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String query = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<com.mintifi.ceoos.data.model.Priority> priorities = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<com.mintifi.ceoos.data.model.TaskStatus> statuses = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer sessionId = null;
    
    public TaskFilter(@org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    java.util.Set<? extends com.mintifi.ceoos.data.model.Priority> priorities, @org.jetbrains.annotations.NotNull()
    java.util.Set<? extends com.mintifi.ceoos.data.model.TaskStatus> statuses, @org.jetbrains.annotations.Nullable()
    java.lang.Integer sessionId) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getQuery() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<com.mintifi.ceoos.data.model.Priority> getPriorities() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<com.mintifi.ceoos.data.model.TaskStatus> getStatuses() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getSessionId() {
        return null;
    }
    
    public TaskFilter() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<com.mintifi.ceoos.data.model.Priority> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<com.mintifi.ceoos.data.model.TaskStatus> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.mintifi.ceoos.data.model.TaskFilter copy(@org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    java.util.Set<? extends com.mintifi.ceoos.data.model.Priority> priorities, @org.jetbrains.annotations.NotNull()
    java.util.Set<? extends com.mintifi.ceoos.data.model.TaskStatus> statuses, @org.jetbrains.annotations.Nullable()
    java.lang.Integer sessionId) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}