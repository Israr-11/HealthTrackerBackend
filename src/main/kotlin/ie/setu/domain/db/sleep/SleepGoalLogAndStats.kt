package ie.setu.domain.db.sleep

import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object SleepGoalLogAndStats: Table("sleep_goal_log_and_stats") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val sleepGoalId = integer("sleep_goal_id").references(SleepGoals.id)
    val actualSleepHours = integer("actual_sleep_hours")
    val actualSleepQuality = varchar("actual_sleep_quality", 50)
    val actualSleepTiming = varchar("actual_sleep_timing", 50)
    val sleepInterruptions = integer("sleep_interruptions")
    val sleepPhaseDetails = varchar("sleep_phase_details", 255)
    val sleepQualityIndex = integer("sleep_quality_index")
    val remarks = varchar("remarks", 255)
    val entryTime = datetime("entry_time")


    override val primaryKey = PrimaryKey(id, name = "PK_SleepGoalLogAndStats_ID")
}