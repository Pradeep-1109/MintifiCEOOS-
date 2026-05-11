package com.mintifi.ceoos.data.database
import android.content.Context
import androidx.room.*
import com.mintifi.ceoos.data.model.*
import kotlinx.coroutines.flow.Flow

class Converters {
    @TypeConverter fun fromPriority(v: Priority) = v.name
    @TypeConverter fun toPriority(v: String) = Priority.valueOf(v)
    @TypeConverter fun fromTaskStatus(v: TaskStatus) = v.name
    @TypeConverter fun toTaskStatus(v: String) = TaskStatus.valueOf(v)
    @TypeConverter fun fromCategory(v: Category) = v.name
    @TypeConverter fun toCategory(v: String) = Category.valueOf(v)
    @TypeConverter fun fromSessionStatus(v: SessionStatus) = v.name
    @TypeConverter fun toSessionStatus(v: String) = SessionStatus.valueOf(v)
    @TypeConverter fun fromReminderCategory(v: ReminderCategory) = v.name
    @TypeConverter fun toReminderCategory(v: String) = ReminderCategory.valueOf(v)
    @TypeConverter fun fromRepeatType(v: RepeatType) = v.name
    @TypeConverter fun toRepeatType(v: String) = RepeatType.valueOf(v)
    @TypeConverter fun fromTodoListType(v: TodoListType) = v.name
    @TypeConverter fun toTodoListType(v: String) = TodoListType.valueOf(v)
    @TypeConverter fun fromTodoPriority(v: TodoPriority) = v.name
    @TypeConverter fun toTodoPriority(v: String) = TodoPriority.valueOf(v)
}

@Dao interface SessionDao {
    @Query("SELECT * FROM sessions WHERE isHidden = 0 ORDER BY date DESC") fun getAll(): Flow<List<Session>>
    @Query("SELECT * FROM sessions ORDER BY date DESC") fun getAllIncludingHidden(): Flow<List<Session>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(s: Session): Long
    @Update suspend fun update(s: Session)
    @Delete suspend fun delete(s: Session)
    @Query("UPDATE sessions SET isHidden = 1 WHERE id = :id") suspend fun hide(id: Int)
    @Query("UPDATE sessions SET isHidden = 0 WHERE id = :id") suspend fun unhide(id: Int)
}
@Dao interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY priority ASC, createdAt DESC") fun getAll(): Flow<List<Task>>
    @Query("SELECT * FROM tasks WHERE sessionId = :sid") fun getBySession(sid: Int): Flow<List<Task>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(t: Task): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(tasks: List<Task>)
    @Update suspend fun update(t: Task)
    @Delete suspend fun delete(t: Task)
    @Query("UPDATE tasks SET status = :status WHERE id = :id") suspend fun updateStatus(id: Int, status: TaskStatus)
}
@Dao interface ReminderDao {
    @Query("SELECT * FROM reminders ORDER BY timeHour ASC, timeMinute ASC") fun getAll(): Flow<List<Reminder>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(r: Reminder): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(items: List<Reminder>)
    @Update suspend fun update(r: Reminder)
    @Delete suspend fun delete(r: Reminder)
    @Query("UPDATE reminders SET isActive = :active WHERE id = :id") suspend fun setActive(id: Int, active: Boolean)
}
@Dao interface TodoDao {
    @Query("SELECT * FROM todos ORDER BY priority ASC, createdAt DESC") fun getAll(): Flow<List<TodoItem>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(item: TodoItem): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(items: List<TodoItem>)
    @Update suspend fun update(item: TodoItem)
    @Delete suspend fun delete(item: TodoItem)
    @Query("UPDATE todos SET isCompleted = :done WHERE id = :id") suspend fun setCompleted(id: Int, done: Boolean)
    @Query("DELETE FROM todos WHERE isCompleted = 1 AND listType = :listType") suspend fun clearCompleted(listType: TodoListType)
}
@Dao interface ChatDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC") fun getAll(): Flow<List<ChatMessage>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(msg: ChatMessage): Long
    @Query("DELETE FROM chat_messages") suspend fun clearAll()
}

@Database(entities = [Session::class, Task::class, Reminder::class, TodoItem::class, ChatMessage::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun taskDao(): TaskDao
    abstract fun reminderDao(): ReminderDao
    abstract fun todoDao(): TodoDao
    abstract fun chatDao(): ChatDao
    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "mintifi_ceo_os.db")
                    .fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
    }
}
