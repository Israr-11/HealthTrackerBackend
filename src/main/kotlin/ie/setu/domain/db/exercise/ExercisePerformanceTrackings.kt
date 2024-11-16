package ie.setu.domain.db.exercise

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime


object ExercisePerformanceTrackings :Table ("exercise_performance_tracking") {
    val id = integer("id").autoIncrement()
    val userPerId = integer("user_per_id")
    val performanceStatus = varchar("performance_status", 30)
    val achievedCount = integer("achieved_count")
    val missCount = integer("miss_count")
    val checkedAt = datetime("checked_at")

    override val primaryKey = PrimaryKey(id, name = "PK_ExercisePerformanceTracking_ID")
}
