package ie.setu.domain.db.health

import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object HealthGoal: Table("health_goal") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val healthGoalType = varchar("health_goal_type", 50)
    val targetValue = integer("target_value")
    val entryTime = datetime("entry_time")

    override val primaryKey = PrimaryKey(id, name = "PK_HealthGoals_ID")
}