package com.mintifi.ceoos;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0007\u000b\f\r\u000e\u000f\u0010\u0011B\u0017\b\u0004\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u0082\u0001\u0007\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u00a8\u0006\u0019"}, d2 = {"Lcom/mintifi/ceoos/Screen;", "", "label", "", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "(Ljava/lang/String;Landroidx/compose/ui/graphics/vector/ImageVector;)V", "getIcon", "()Landroidx/compose/ui/graphics/vector/ImageVector;", "getLabel", "()Ljava/lang/String;", "Copilot", "Dashboard", "Record", "Reminders", "Settings", "Tasks", "Todo", "Lcom/mintifi/ceoos/Screen$Copilot;", "Lcom/mintifi/ceoos/Screen$Dashboard;", "Lcom/mintifi/ceoos/Screen$Record;", "Lcom/mintifi/ceoos/Screen$Reminders;", "Lcom/mintifi/ceoos/Screen$Settings;", "Lcom/mintifi/ceoos/Screen$Tasks;", "Lcom/mintifi/ceoos/Screen$Todo;", "app_debug"})
public abstract class Screen {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String label = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.ui.graphics.vector.ImageVector icon = null;
    
    private Screen(java.lang.String label, androidx.compose.ui.graphics.vector.ImageVector icon) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLabel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getIcon() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/mintifi/ceoos/Screen$Copilot;", "Lcom/mintifi/ceoos/Screen;", "()V", "app_debug"})
    public static final class Copilot extends com.mintifi.ceoos.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.mintifi.ceoos.Screen.Copilot INSTANCE = null;
        
        private Copilot() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/mintifi/ceoos/Screen$Dashboard;", "Lcom/mintifi/ceoos/Screen;", "()V", "app_debug"})
    public static final class Dashboard extends com.mintifi.ceoos.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.mintifi.ceoos.Screen.Dashboard INSTANCE = null;
        
        private Dashboard() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/mintifi/ceoos/Screen$Record;", "Lcom/mintifi/ceoos/Screen;", "()V", "app_debug"})
    public static final class Record extends com.mintifi.ceoos.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.mintifi.ceoos.Screen.Record INSTANCE = null;
        
        private Record() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/mintifi/ceoos/Screen$Reminders;", "Lcom/mintifi/ceoos/Screen;", "()V", "app_debug"})
    public static final class Reminders extends com.mintifi.ceoos.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.mintifi.ceoos.Screen.Reminders INSTANCE = null;
        
        private Reminders() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/mintifi/ceoos/Screen$Settings;", "Lcom/mintifi/ceoos/Screen;", "()V", "app_debug"})
    public static final class Settings extends com.mintifi.ceoos.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.mintifi.ceoos.Screen.Settings INSTANCE = null;
        
        private Settings() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/mintifi/ceoos/Screen$Tasks;", "Lcom/mintifi/ceoos/Screen;", "()V", "app_debug"})
    public static final class Tasks extends com.mintifi.ceoos.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.mintifi.ceoos.Screen.Tasks INSTANCE = null;
        
        private Tasks() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/mintifi/ceoos/Screen$Todo;", "Lcom/mintifi/ceoos/Screen;", "()V", "app_debug"})
    public static final class Todo extends com.mintifi.ceoos.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.mintifi.ceoos.Screen.Todo INSTANCE = null;
        
        private Todo() {
        }
    }
}