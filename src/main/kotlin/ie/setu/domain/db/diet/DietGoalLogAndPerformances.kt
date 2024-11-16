package ie.setu.domain.db.diet

import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object DietGoalLogAndPerformances: Table("diet_goal_log_and_performances") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val dietGoalId = integer("diet_goal_id").references(DietGoals.id)
    val caloriesConsumed = integer("calories_consumed")
    val recommendations = varchar("recommendations", 255)
    val status = varchar("status", 255)
    val targetReached = bool("target_reached")
    val deficitSurplus = varchar("deficit_surplus", 255)
    val entryTime = datetime("entry_time")

    override val primaryKey = PrimaryKey(id, name = "PK_DietGoalLogAndPerformances_ID")

}