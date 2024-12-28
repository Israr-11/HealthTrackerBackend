package ie.setu.controllers

import ie.setu.domain.screentime.ScreenTimeGoal
import ie.setu.domain.repository.screentime.ScreenTimeGoalDAO
import ie.setu.domain.repository.screentime.ScreenTimeLogAndPerformanceDAO
import ie.setu.domain.screentime.ScreenTimeLogAndPerformance
import ie.setu.utils.jsonToObject
import io.javalin.http.Context
import org.joda.time.DateTime

object ScreenTimeTrackerController {
    private val screenTimeGoalDAO = ScreenTimeGoalDAO()
    private val screenTimeGoalLogAndPerformanceDAO = ScreenTimeLogAndPerformanceDAO()

    //Screen Time Goal Controllers

    fun getAllScreenTimeGoal(ctx: Context) {
        val goal = screenTimeGoalDAO.getAll()
        if (goal.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(goal)
    }

    fun getScreenTimeGoalsByUserId(ctx: Context) {

        val logs = screenTimeGoalDAO.findByUserId(ctx.pathParam("user-id").toInt())
        if (logs.isNotEmpty()) {
            ctx.json(logs)
            ctx.status(200)
        } else {
            ctx.status(404)
        }

    }

    fun addScreenTimeGoal(ctx: Context) {
        val screen_time_goal: ScreenTimeGoal = jsonToObject(ctx.body())
        val screenTimeGoalId = screenTimeGoalDAO.save(screen_time_goal)
        if (screenTimeGoalId != null) {
            screen_time_goal.id = screenTimeGoalId
            ctx.json(
                mapOf(
                    "id" to screen_time_goal.id,
                    "userId" to screen_time_goal.userId,
                    "targetScreenHours" to screen_time_goal.targetScreenHours,
                    "entryTime" to DateTime.now()
                )
            )
            ctx.status(201)
        }
    }

    fun updateScreenTimeGoalByGoalId(ctx: Context){
        val screentimegoal : ScreenTimeGoal = jsonToObject(ctx.body())
        if (screenTimeGoalDAO.updateByScreenTimeGoalId(
                screenTimeGoalId = ctx.pathParam("screen-time-goal-id").toInt(),
                screenTimeGoalToUpdate = screentimegoal) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }


    //Screen Time Log and Performance Controllers


    fun addScreenTimeLogAndPerformance(ctx: Context) {
        val screen_time_log_and_performance: ScreenTimeLogAndPerformance = jsonToObject(ctx.body())
        val screentimelogId = screenTimeGoalLogAndPerformanceDAO.save(screen_time_log_and_performance)
        if (screentimelogId != null) {
            screen_time_log_and_performance.id = screentimelogId
            ctx.json(
                mapOf(
                    "id" to screen_time_log_and_performance.id,
                    "userId" to screen_time_log_and_performance.userId,
                    "screenTimeGoalId" to screen_time_log_and_performance.screenTimeGoalId,
                    "actualScreenHours " to screen_time_log_and_performance.actualScreenHours
                )
            )
            ctx.status(201)
        }
        else{
            ctx.status(500)
        }
    }



    fun getScreenTimePerformanceByUserId(ctx: Context) {

        val performance = screenTimeGoalLogAndPerformanceDAO.findByUserId(ctx.pathParam("user-id").toInt())
        if (performance.isNotEmpty()) {
            ctx.json(performance)
            ctx.status(200)
        } else {
            ctx.status(404)
        }

    }

    fun deleteByScreenTimeGoalId(ctx: Context){
        if (screenTimeGoalLogAndPerformanceDAO.deleteByScreenTimeGoalId(ctx.pathParam("screen_time_goal_id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }


}


