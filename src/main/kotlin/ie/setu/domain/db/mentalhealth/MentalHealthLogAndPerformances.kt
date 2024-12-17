package ie.setu.domain.db.mentalhealth

import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object MentalHealthLogAndPerformances : Table("mental_health_log_and_performances") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val mentalHealthGoalId = integer("mental_health_goal_id").references(MentalHealthGoals.id)
    val moodScore = integer("mood_score")
    val stressLevel = integer("stress_level")
    val targetMet = bool("target_met")
    val recommendations = varchar("recommendations", 255).nullable()
    val entryTime = datetime("entry_time")

    override val primaryKey = PrimaryKey(id, name = "PK_MentalHealthLogAndPerformances_ID")
}
