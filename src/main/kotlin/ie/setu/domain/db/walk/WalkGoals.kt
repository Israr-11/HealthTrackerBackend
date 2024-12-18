package ie.setu.domain.db.walk

import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object WalkGoals : Table("walk_goals") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val targetSteps = integer("target_steps")
    val uphill = bool("uphill")
    val entryTime = datetime("entry_time")

    override val primaryKey = PrimaryKey(id, name = "PK_WalkGoals_ID")
}
