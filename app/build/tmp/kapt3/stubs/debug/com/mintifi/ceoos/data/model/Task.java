package com.mintifi.ceoos.data.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u001f\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001Bs\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0006\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0012\u00a2\u0006\u0002\u0010\u0013J\t\u0010%\u001a\u00020\u0003H\u00c6\u0003J\t\u0010&\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\'\u001a\u00020\u0012H\u00c6\u0003J\t\u0010(\u001a\u00020\u0003H\u00c6\u0003J\t\u0010)\u001a\u00020\u0006H\u00c6\u0003J\t\u0010*\u001a\u00020\u0006H\u00c6\u0003J\t\u0010+\u001a\u00020\tH\u00c6\u0003J\t\u0010,\u001a\u00020\u000bH\u00c6\u0003J\t\u0010-\u001a\u00020\rH\u00c6\u0003J\t\u0010.\u001a\u00020\u0006H\u00c6\u0003J\t\u0010/\u001a\u00020\u0006H\u00c6\u0003Jw\u00100\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u00062\b\b\u0002\u0010\u0010\u001a\u00020\u00062\b\b\u0002\u0010\u0011\u001a\u00020\u0012H\u00c6\u0001J\u0013\u00101\u001a\u0002022\b\u00103\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u00104\u001a\u00020\u0003H\u00d6\u0001J\t\u00105\u001a\u00020\u0006H\u00d6\u0001R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0011\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u000f\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u0007\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0019R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\u000e\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0019R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0011\u0010\u0010\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0019R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001cR\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u0019\u00a8\u00066"}, d2 = {"Lcom/mintifi/ceoos/data/model/Task;", "", "id", "", "sessionId", "title", "", "detail", "priority", "Lcom/mintifi/ceoos/data/model/Priority;", "category", "Lcom/mintifi/ceoos/data/model/Category;", "status", "Lcom/mintifi/ceoos/data/model/TaskStatus;", "owner", "deadline", "projectGroup", "createdAt", "", "(IILjava/lang/String;Ljava/lang/String;Lcom/mintifi/ceoos/data/model/Priority;Lcom/mintifi/ceoos/data/model/Category;Lcom/mintifi/ceoos/data/model/TaskStatus;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;J)V", "getCategory", "()Lcom/mintifi/ceoos/data/model/Category;", "getCreatedAt", "()J", "getDeadline", "()Ljava/lang/String;", "getDetail", "getId", "()I", "getOwner", "getPriority", "()Lcom/mintifi/ceoos/data/model/Priority;", "getProjectGroup", "getSessionId", "getStatus", "()Lcom/mintifi/ceoos/data/model/TaskStatus;", "getTitle", "component1", "component10", "component11", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
@androidx.room.Entity(tableName = "tasks")
public final class Task {
    @androidx.room.PrimaryKey(autoGenerate = true)
    private final int id = 0;
    private final int sessionId = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String title = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String detail = null;
    @org.jetbrains.annotations.NotNull()
    private final com.mintifi.ceoos.data.model.Priority priority = null;
    @org.jetbrains.annotations.NotNull()
    private final com.mintifi.ceoos.data.model.Category category = null;
    @org.jetbrains.annotations.NotNull()
    private final com.mintifi.ceoos.data.model.TaskStatus status = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String owner = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String deadline = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String projectGroup = null;
    private final long createdAt = 0L;
    
    public Task(int id, int sessionId, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String detail, @org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.Priority priority, @org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.Category category, @org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.TaskStatus status, @org.jetbrains.annotations.NotNull()
    java.lang.String owner, @org.jetbrains.annotations.NotNull()
    java.lang.String deadline, @org.jetbrains.annotations.NotNull()
    java.lang.String projectGroup, long createdAt) {
        super();
    }
    
    public final int getId() {
        return 0;
    }
    
    public final int getSessionId() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDetail() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.mintifi.ceoos.data.model.Priority getPriority() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.mintifi.ceoos.data.model.Category getCategory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.mintifi.ceoos.data.model.TaskStatus getStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOwner() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDeadline() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getProjectGroup() {
        return null;
    }
    
    public final long getCreatedAt() {
        return 0L;
    }
    
    public Task() {
        super();
    }
    
    public final int component1() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component10() {
        return null;
    }
    
    public final long component11() {
        return 0L;
    }
    
    public final int component2() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.mintifi.ceoos.data.model.Priority component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.mintifi.ceoos.data.model.Category component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.mintifi.ceoos.data.model.TaskStatus component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.mintifi.ceoos.data.model.Task copy(int id, int sessionId, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String detail, @org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.Priority priority, @org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.Category category, @org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.TaskStatus status, @org.jetbrains.annotations.NotNull()
    java.lang.String owner, @org.jetbrains.annotations.NotNull()
    java.lang.String deadline, @org.jetbrains.annotations.NotNull()
    java.lang.String projectGroup, long createdAt) {
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