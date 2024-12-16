package ie.setu.domain.db.water

import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object WaterGoalLogAndStats: Table("water_goal_log_and_stats") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val waterGoalId = integer("water_goal_id").references(WaterGoals.id)
    val actualWaterIntake = integer("actual_water_intake")
    val targetMet = bool("target_met")
    val shortfall = integer("shortfall")
    val recommendations = varchar("recommendations", 255)
    val entryTime = datetime("entry_time")

    override val primaryKey = PrimaryKey(id, name = "PK_WaterGoalLogAndStats_ID")

}