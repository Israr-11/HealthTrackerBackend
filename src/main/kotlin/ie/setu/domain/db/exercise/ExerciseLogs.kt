package ie.setu.domain.db.exercise

import ie.setu.domain.db.Users

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object ExerciseLogs : Table("exercise_logs") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val exerciseId = integer("exercise_id").references(ExerciseSchedules.id)
    val status = varchar("status", 20)
    val entryTime = datetime("entry_time")

    override val primaryKey = PrimaryKey(id, name = "PK_ExerciseLogs_ID")
}
