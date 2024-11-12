

package ie.setu.domain.repository.exercise

import ie.setu.domain.db.Users
import ie.setu.domain.exercise.ExerciseLog
import ie.setu.domain.db.exercise.ExerciseLogs
import ie.setu.domain.db.exercise.ExerciseSchedules
import ie.setu.utils.mapToExerciseLog
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime


class ExerciseLogDAO {


    fun getAll():  ArrayList<ExerciseLog> {
        val exerciselogList: ArrayList<ExerciseLog> = arrayListOf()

        transaction {
            ExerciseLogs.selectAll().map {
                exerciselogList.add(mapToExerciseLog(it))
            }
        }
        return exerciselogList
    }




    fun save(exercise_log: ExerciseLog) : Int?{
        return transaction {
            val exerciselogId=ExerciseLogs.insert {
                it[userId] = exercise_log.userId
                it[exerciseId] = exercise_log.exerciseId
                it[status] = exercise_log.status
                it[entryTime] = DateTime.now()
            } get ExerciseLogs.id
            exerciselogId
        }
    }


    fun deleteByExerciseLogId(exerciselogId: Int): Int {
        return transaction {
            ExerciseLogs.deleteWhere { ExerciseLogs.id eq exerciselogId }
        }
    }



}
