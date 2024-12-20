package ie.setu.domain.repository.exercise

import ie.setu.domain.User
import ie.setu.domain.db.Users
import ie.setu.domain.db.exercise.ExercisePerformanceTrackings
import ie.setu.domain.exercise.ExerciseSchedule
import ie.setu.domain.db.exercise.ExerciseSchedules
import ie.setu.domain.exercise.ExercisePerformanceTracking
import ie.setu.utils.mapToExercisePerformanceTracking
import ie.setu.utils.mapToExerciseSchedule
import ie.setu.utils.mapToUser
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.joda.time.DateTime


class ExerciseScheduleDAO {


    fun getAll():  ArrayList<ExerciseSchedule> {
        val exercisescheduleList: ArrayList<ExerciseSchedule> = arrayListOf()

        transaction {
            ExerciseSchedules.selectAll().map {
                exercisescheduleList.add(mapToExerciseSchedule(it))
            }
        }
        return exercisescheduleList
    }

    fun findByUserId(userId: Int): List<ExerciseSchedule>{
        return transaction {
            ExerciseSchedules
                .selectAll().where { ExerciseSchedules.userId eq userId}
                .map { mapToExerciseSchedule(it) }
        }
    }



    fun save(exercise_schedule: ExerciseSchedule) : Int?{
        return transaction {
            val exercisescheduleId=ExerciseSchedules.insert {
                it[userId] = exercise_schedule.userId
                it[exerciseType] = exercise_schedule.exerciseType
                it[duration] = exercise_schedule.duration
                it[entryTime] = DateTime.now()
            } get ExerciseSchedules.id
            exercisescheduleId
        }
    }


    fun deleteByExerciseScheduleId(exercisescheduleId: Int): Int {
        return transaction {
            ExerciseSchedules.deleteWhere { ExerciseSchedules.id eq exercisescheduleId }
        }
    }



}
