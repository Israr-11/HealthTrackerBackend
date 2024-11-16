package ie.setu.controllers

import ie.setu.domain.exercise.ExerciseLog
import ie.setu.domain.exercise.ExerciseSchedule
import ie.setu.domain.repository.exercise.ExerciseLogDAO
import ie.setu.domain.repository.exercise.ExercisePerformanceTrackingDAO
import ie.setu.domain.repository.exercise.ExerciseScheduleDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context


object ExerciseTrackerController {
    private val exercisescheduleDAO = ExerciseScheduleDAO()
    private val exerciselogDAO = ExerciseLogDAO()
    private val exercisePerformanceTrackingDAO = ExercisePerformanceTrackingDAO()

    //Exercise Schedule Controllers

    fun getAllExerciseSchedules(ctx: Context) {
        val schedules = exercisescheduleDAO.getAll()
        if (schedules.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(schedules)
    }


    fun addExerciseSchedule(ctx: Context) {
        val exercise_schedule: ExerciseSchedule = jsonToObject(ctx.body())
        val exercisescheduleId = exercisescheduleDAO.save(exercise_schedule)
        if (exercisescheduleId != null) {
            exercise_schedule.id = exercisescheduleId
            ctx.json(
                mapOf(
                    "id" to exercise_schedule.id,
                    "userId" to exercise_schedule.userId,
                    "exerciseType" to exercise_schedule.exerciseType,
                    "status" to exercise_schedule.duration
                )
            )
            ctx.status(201)
        }
    }

    fun deleteExerciseSchedule(ctx: Context) {
        if (exercisescheduleDAO.deleteByExerciseScheduleId(ctx.pathParam("exercise-schedule-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }


    //Exercise Log Controllers

    fun getAllExerciseLogs(ctx: Context) {
        val logs = exerciselogDAO.getAll()
        if (logs.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(logs)
    }

    fun addExerciseLog(ctx: Context) {
        val exercise_log: ExerciseLog = jsonToObject(ctx.body())
        val exerciselogId = exerciselogDAO.save(exercise_log)
        if (exerciselogId != null) {
            exercise_log.id = exerciselogId
            ctx.json(
                mapOf(
                    "id" to exercise_log.id,
                    "userId" to exercise_log.userId,
                    "exerciseId" to exercise_log.exerciseId,
                    "status" to exercise_log.status,
                )
            )
            ctx.status(201)
        }
    }

    fun deleteEexerciseLog(ctx: Context) {
        if (exerciselogDAO.deleteByExerciseLogId(ctx.pathParam("exercise-log-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun calculateAndSaveUserPerformance(ctx: Context) {
        val userId = ctx.pathParam("user-id").toInt()
        exercisePerformanceTrackingDAO.calculateUserPerformance(userId)

        ctx.status(201)
    }


    fun getPerformanceByUserPerId(ctx: Context) {

            val performance = exercisePerformanceTrackingDAO.findByUserPerId(ctx.pathParam("user-per-id").toInt())
            if (performance.isNotEmpty()) {
                ctx.json(performance)
                ctx.status(200)
            } else {
                ctx.status(404)
            }

    }

    fun deleteByUserPerId(ctx: Context){
        if (exercisePerformanceTrackingDAO.deleteByUserPerId(ctx.pathParam("user-per-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}

