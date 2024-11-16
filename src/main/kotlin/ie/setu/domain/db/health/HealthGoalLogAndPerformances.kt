package ie.setu.domain.db.health

import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object HealthGoalLogAndPerformances: Table("health_goal_and_log_performances") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val healthGoalId = integer("health_goal_id").references(HealthGoals.id)
    val achievedValue = integer("achieved_value")
    val status = varchar("status", 20)
    val targetReached = bool("target_reached")
    val remarks = text("remarks")
    val entryTime = datetime("entry_time")

    override val primaryKey = PrimaryKey(id, name = "PK_HealthGoalLogAndPerformances_ID")
}