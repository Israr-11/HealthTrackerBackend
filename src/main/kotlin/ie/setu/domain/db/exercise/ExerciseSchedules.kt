package ie.setu.domain.db.exercise

import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object ExerciseSchedules : Table("exercise_schedule") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val exerciseType = varchar("exercise_type", 100)
    val duration = integer("duration")
    val entryTime = datetime("entry_time")

    override val primaryKey = PrimaryKey(id, name = "PK_ExerciseSchedule_ID")
}

