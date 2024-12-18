package ie.setu.domain.db.walk

import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object WalkGoalLogAndStats : Table("walk_goal_log_and_stats") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val walkGoalId = integer("walk_goal_id").references(WalkGoals.id)
    val actualSteps = integer("actual_steps")
    val targetMet = bool("target_met")
    val extraSteps = integer("extra_steps").nullable()
    val walkQuality = varchar("walk_quality", 255).nullable()
    val recommendations = varchar("recommendations", 255).nullable()
    val entryTime = datetime("entry_time")

    override val primaryKey = PrimaryKey(id, name = "PK_WalkGoalLogAndStats_ID")
}
