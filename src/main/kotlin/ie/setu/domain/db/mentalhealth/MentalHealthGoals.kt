package ie.setu.domain.db.mentalhealth

import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object MentalHealthGoals : Table("mental_health_goals") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val targetMoodScore = integer("target_mood_score")
    val entryTime = datetime("entry_time")

    override val primaryKey = PrimaryKey(id, name = "PK_MentalHealthGoals_ID")
}
