package ie.setu.domain.db.screentime

import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object ScreenTimeLogAndPerformances : Table("screen_time_log_and_performances") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val screenTimeGoalId = integer("screen_time_goal_id").references(ScreenTimeGoals.id)
    val actualScreenHours = integer("actual_screen_hours")
    val targetMet = bool("target_met")
    val extraHours = integer("extra_hours")
    val recommendations = varchar("recommendations", 255)
    val entryTime = datetime("entry_time")

    override val primaryKey = PrimaryKey(id, name = "PK_ScreenTimeLogAndPerformances_ID")
}