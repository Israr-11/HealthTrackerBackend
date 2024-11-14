package ie.setu.domain.db.diet

import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object DietGoals: Table("diet_goals") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val dietGoalType = varchar("diet_goal_type", 255)
    val targetCalories = integer("target_calories")
    val entryTime = datetime("entry_time")

    override val primaryKey = PrimaryKey(id, name = "PK_DietGoals_ID")
}