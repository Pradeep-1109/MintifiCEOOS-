package com.mintifi.ceoos.ui.screens;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000z\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a4\u0010\u0000\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001a\u0010\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\nH\u0007\u001a@\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u00122\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0014\u0010\u0015\u001a2\u0010\u0016\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0018\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0012H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0019\u0010\u001a\u001aH\u0010\u001b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\n2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0007\u001a2\u0010 \u001a\u00020\u00012\u0006\u0010!\u001a\u00020\"2\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001a\u0010\u0010#\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\nH\u0007\u001a\u0010\u0010$\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\nH\u0007\u001a\u0018\u0010%\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0017\u001a\u00020\rH\u0003\u001a<\u0010&\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0017\u001a\u00020\r2\u0006\u0010\'\u001a\u00020\r2\u0006\u0010(\u001a\u00020)2\u0012\u0010*\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a.\u0010+\u001a\u00020\u00012\u0006\u0010,\u001a\u00020\r2\u001c\u0010-\u001a\u0018\u0012\u0004\u0012\u00020.\u0012\u0004\u0012\u00020\u00010\u0005\u00a2\u0006\u0002\b/\u00a2\u0006\u0002\b0H\u0003\u001a\b\u00101\u001a\u00020\u0001H\u0007\u001ad\u00102\u001a\u00020\u00012\u0006\u0010!\u001a\u00020\"2\u0012\u00103\u001a\u000e\u0012\u0004\u0012\u000204\u0012\u0004\u0012\u00020\u00010\u00052\f\u00105\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u00106\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u00107\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u00108\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\u0006\u00109\u001a\u00020:H\u0003\u001a\u0010\u0010;\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\nH\u0007\u001a*\u0010<\u001a\u00020\u00012\u0012\u0010=\u001a\u000e\u0012\u0004\u0012\u00020>\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001a6\u0010?\u001a\u00020\u00012\u0006\u0010@\u001a\u00020A2\f\u0010B\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u00106\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0002\u0010C\u001a\u00020)H\u0003\u001a\u0010\u0010D\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\nH\u0007\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006E"}, d2 = {"AddReminderSheet", "", "existing", "Lcom/mintifi/ceoos/data/model/Reminder;", "onSave", "Lkotlin/Function1;", "onDismiss", "Lkotlin/Function0;", "CopilotScreen", "viewModel", "Lcom/mintifi/ceoos/viewmodel/MainViewModel;", "DActionBtn", "label", "", "bg", "Landroidx/compose/ui/graphics/Color;", "fg", "modifier", "Landroidx/compose/ui/Modifier;", "onClick", "DActionBtn-1wkBAMs", "(Ljava/lang/String;JJLandroidx/compose/ui/Modifier;Lkotlin/jvm/functions/Function0;)V", "DStatCard", "value", "color", "DStatCard-9LQNqLg", "(Ljava/lang/String;Ljava/lang/String;JLandroidx/compose/ui/Modifier;)V", "DashboardScreen", "onNavigateToRecord", "onNavigateToCopilot", "onNavigateToTodo", "onNavigateToReminders", "EditTaskSheet", "task", "Lcom/mintifi/ceoos/data/model/Task;", "RecordScreen", "RemindersScreen", "SInfoRow", "SKeyField", "hint", "visible", "", "onChange", "SSection", "title", "content", "Landroidx/compose/foundation/layout/ColumnScope;", "Landroidx/compose/runtime/Composable;", "Lkotlin/ExtensionFunctionType;", "SettingsScreen", "TaskCardWithActions", "onStatusChange", "Lcom/mintifi/ceoos/data/model/TaskStatus;", "onEdit", "onDelete", "onAddTodo", "onAddReminder", "fmt", "Ljava/text/SimpleDateFormat;", "TaskLogScreen", "TemplatesSheet", "onSelect", "Lcom/mintifi/ceoos/data/model/WorkTemplate;", "TodoCard", "todo", "Lcom/mintifi/ceoos/data/model/TodoItem;", "onCheck", "done", "TodoScreen", "app_debug"})
public final class ScreensKt {
    
    @androidx.compose.runtime.Composable()
    public static final void DashboardScreen(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.viewmodel.MainViewModel viewModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateToRecord, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateToCopilot, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateToTodo, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateToReminders) {
    }
    
    @kotlin.OptIn(markerClass = {com.google.accompanist.permissions.ExperimentalPermissionsApi.class})
    @androidx.compose.runtime.Composable()
    public static final void RecordScreen(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.viewmodel.MainViewModel viewModel) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void TaskLogScreen(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.viewmodel.MainViewModel viewModel) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void TaskCardWithActions(com.mintifi.ceoos.data.model.Task task, kotlin.jvm.functions.Function1<? super com.mintifi.ceoos.data.model.TaskStatus, kotlin.Unit> onStatusChange, kotlin.jvm.functions.Function0<kotlin.Unit> onEdit, kotlin.jvm.functions.Function0<kotlin.Unit> onDelete, kotlin.jvm.functions.Function0<kotlin.Unit> onAddTodo, kotlin.jvm.functions.Function0<kotlin.Unit> onAddReminder, java.text.SimpleDateFormat fmt) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void EditTaskSheet(com.mintifi.ceoos.data.model.Task task, kotlin.jvm.functions.Function1<? super com.mintifi.ceoos.data.model.Task, kotlin.Unit> onSave, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void CopilotScreen(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.viewmodel.MainViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void RemindersScreen(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.viewmodel.MainViewModel viewModel) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void AddReminderSheet(com.mintifi.ceoos.data.model.Reminder existing, kotlin.jvm.functions.Function1<? super com.mintifi.ceoos.data.model.Reminder, kotlin.Unit> onSave, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void TodoScreen(@org.jetbrains.annotations.NotNull()
    com.mintifi.ceoos.viewmodel.MainViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TodoCard(com.mintifi.ceoos.data.model.TodoItem todo, kotlin.jvm.functions.Function0<kotlin.Unit> onCheck, kotlin.jvm.functions.Function0<kotlin.Unit> onDelete, boolean done) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void TemplatesSheet(kotlin.jvm.functions.Function1<? super com.mintifi.ceoos.data.model.WorkTemplate, kotlin.Unit> onSelect, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void SettingsScreen() {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SSection(java.lang.String title, kotlin.jvm.functions.Function1<? super androidx.compose.foundation.layout.ColumnScope, kotlin.Unit> content) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SKeyField(java.lang.String label, java.lang.String value, java.lang.String hint, boolean visible, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onChange) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SInfoRow(java.lang.String label, java.lang.String value) {
    }
}