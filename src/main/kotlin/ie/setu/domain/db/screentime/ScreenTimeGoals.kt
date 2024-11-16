package ie.setu.domain.db.screentime

import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object ScreenTimeGoals: Table("screen_time_goals") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val targetScreenHours = integer("target_screen_hours")
    val entryTime = datetime("entry_time")

    override val primaryKey = PrimaryKey(id, name = "PK_ScreenTimeGoals_ID")
}
