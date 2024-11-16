package ie.setu.domain.db.sleep

import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object SleepGoals:Table("sleep_goals") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val targetSleepHours = integer("target_sleep_hours")
    val targetSleepQuality = varchar("target_sleep_quality", 50)
    val targetSleepTiming = varchar("target_sleep_timing", 50)
    val entryTime = datetime("entry_time")

    override val primaryKey = PrimaryKey(id, name = "PK_SleepGoals_ID")
}