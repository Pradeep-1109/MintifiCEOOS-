package com.mintifi.ceoos.data.database;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\bH\u0007J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\nH\u0007J\u0010\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\fH\u0007J\u0010\u0010\r\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u000eH\u0007J\u0010\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0010H\u0007J\u0010\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0012H\u0007J\u0010\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0014H\u0007J\u0010\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u0017\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u0018\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u0019\u001a\u00020\u000e2\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u001a\u001a\u00020\u00102\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u001c\u001a\u00020\u00142\u0006\u0010\u0005\u001a\u00020\u0004H\u0007\u00a8\u0006\u001d"}, d2 = {"Lcom/mintifi/ceoos/data/database/Converters;", "", "()V", "fromCategory", "", "v", "Lcom/mintifi/ceoos/data/model/Category;", "fromPriority", "Lcom/mintifi/ceoos/data/model/Priority;", "fromReminderCategory", "Lcom/mintifi/ceoos/data/model/ReminderCategory;", "fromRepeatType", "Lcom/mintifi/ceoos/data/model/RepeatType;", "fromSessionStatus", "Lcom/mintifi/ceoos/data/model/SessionStatus;", "fromTaskStatus", "Lcom/mintifi/ceoos/data/model/TaskStatus;", "fromTodoListType", "Lcom/mintifi/ceoos/data/model/TodoListType;", "fromTodoPriority", "Lcom/mintifi/ceoos/data/model/TodoPriority;", "toCategory", "toPriority", "toReminderCategory", "toRepeatType", "toSessionStatus", "toTaskStatus", "toTodoListType", "toTodoPriority", "app_debug"})
public final class Converters {
    
    public Converters() {
        super();
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String fromPriority(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.Priority v) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final com.mintifi.ceoos.data.model.Priority toPriority(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String fromTaskStatus(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.TaskStatus v) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final com.mintifi.ceoos.data.model.TaskStatus toTaskStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String fromCategory(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.Category v) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final com.mintifi.ceoos.data.model.Category toCategory(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String fromSessionStatus(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.SessionStatus v) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final com.mintifi.ceoos.data.model.SessionStatus toSessionStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String fromReminderCategory(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.ReminderCategory v) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final com.mintifi.ceoos.data.model.ReminderCategory toReminderCategory(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String fromRepeatType(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.RepeatType v) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final com.mintifi.ceoos.data.model.RepeatType toRepeatType(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String fromTodoListType(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.TodoListType v) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final com.mintifi.ceoos.data.model.TodoListType toTodoListType(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String fromTodoPriority(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.data.model.TodoPriority v) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final com.mintifi.ceoos.data.model.TodoPriority toTodoPriority(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
        return null;
    }
}