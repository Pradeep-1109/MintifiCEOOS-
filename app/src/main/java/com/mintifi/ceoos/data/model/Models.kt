package com.mintifi.ceoos.data.model
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Priority(val label: String) { P0("P0"), P1("P1"), P2("P2"), P3("P3") }
enum class TaskStatus(val label: String) {
    PENDING("Pending"), IN_PROGRESS("In Progress"), COMPLETED("Completed"),
    WAITING("Waiting"), UNDER_REVIEW("Review"), DROPPED("Dropped"), ESCALATED("Escalated")
}
enum class Category(val label: String, val emoji: String) {
    CEO_ASK("CEO Ask","👑"), DDR("DDR","📊"), LAP("LAP","🏠"),
    COLLECTIONS("Collections","💰"), AI_PROJECTS("AI Projects","🤖"),
    TREDS("TReDS","🔄"), MBA("MBA","📚"), GENERAL("General","📌")
}
enum class SessionStatus { RECORDING, PROCESSING, DONE, ERROR }
enum class ReminderCategory(val label: String, val emoji: String) {
    CEO_PREP("CEO Prep","👑"), WORK("Work","💼"), MBA("MBA","📚"),
    PERSONAL("Personal","👤"), GENERAL("General","🔔")
}
enum class RepeatType(val label: String) {
    DAILY("Daily"), WEEKDAYS("Weekdays"), WEEKLY("Weekly"), MONTHLY("Monthly"), ONCE("Once")
}
enum class TodoListType(val label: String, val emoji: String) {
    TODAY("Today","📅"), THIS_WEEK("This Week","📆"), MBA("MBA","📚"),
    CEO_PREP("CEO Prep","👑"), PERSONAL("Personal","👤"), WORK("Work","💼")
}
enum class TodoPriority(val label: String) { HIGH("High"), NORMAL("Normal"), LOW("Low") }

@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String = "",
    val date: Long = System.currentTimeMillis(),
    val audioPath: String = "",
    val transcript: String = "",
    val summary: String = "",
    val status: SessionStatus = SessionStatus.RECORDING,
    val taskCount: Int = 0,
    val durationMs: Long = 0L,
    val isHidden: Boolean = false
)

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sessionId: Int = 0,
    val title: String = "",
    val detail: String = "",
    val priority: Priority = Priority.P2,
    val category: Category = Category.GENERAL,
    val status: TaskStatus = TaskStatus.PENDING,
    val owner: String = "Pradeep",
    val deadline: String = "TBD",
    val projectGroup: String = "General",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String = "", val category: ReminderCategory = ReminderCategory.GENERAL,
    val repeatType: RepeatType = RepeatType.WEEKDAYS,
    val timeHour: Int = 9, val timeMinute: Int = 0,
    val isActive: Boolean = true, val notificationId: Int = 0
)

@Entity(tableName = "todos")
data class TodoItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String = "", val listType: TodoListType = TodoListType.TODAY,
    val priority: TodoPriority = TodoPriority.NORMAL,
    val isCompleted: Boolean = false, val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val role: String = "user", val content: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class DashboardStats(
    val total: Int = 0, val p0Count: Int = 0, val p1Count: Int = 0,
    val p2Count: Int = 0, val p3Count: Int = 0, val completedCount: Int = 0
)

data class TaskFilter(
    val query: String = "",
    val priorities: Set<Priority> = emptySet(),
    val statuses: Set<TaskStatus> = emptySet(),
    val sessionId: Int? = null
)

data class WorkTemplate(val title: String, val emoji: String, val items: List<String>)

val PRADEEP_WORK_TEMPLATES = listOf(
    WorkTemplate("CEO Call Prep","👑", listOf("Review previous MOM action items","Prepare DDR portfolio summary","Check collections escalations","Update AI projects status","Prepare key metrics dashboard")),
    WorkTemplate("Morning Routine","🌅", listOf("Check overnight alerts","Review today calendar","Read DDR daily report","Check WhatsApp escalations","Prepare daily standup notes")),
    WorkTemplate("DDR Daily Check","📊", listOf("Review DDR automation dashboard","Check pending approvals","Flag anomalies to risk team","Update DDR tracker sheet","Send daily DDR summary")),
    WorkTemplate("Collections Review","💰", listOf("Check bucket-wise collections","Review escalation cases","Update collections dashboard","Coordinate with field team","Prepare collections MOM")),
    WorkTemplate("MBA Study","📚", listOf("Review today module","Complete assignments","Make study notes","Practice past questions","Submit pending work")),
    WorkTemplate("EOD Wrap","🌆", listOf("Update task status in CEO OS","Send EOD summary to CEO","Plan tomorrow priorities","Clear email inbox","Update personal diary"))
)
