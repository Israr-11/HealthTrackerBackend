package ie.setu.controllers

import ie.setu.domain.sleep.SleepGoal
import ie.setu.domain.sleep.SleepGoalLogAndStat

import ie.setu.domain.repository.sleep.SleepGoalDAO
import ie.setu.domain.repository.sleep.SleepGoalLogAndStatsDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context


object SleepTrackerController {
    private val sleepGoalDAO = SleepGoalDAO()
    private val sleepGoalLogAndStatDAO = SleepGoalLogAndStatsDAO()

    //Sleep Goal Controllers

    fun getAllSleepGoal(ctx: Context) {
        val goal = sleepGoalDAO.getAll()
        if (goal.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(goal)
    }

    fun getSleepGoalByUserId(ctx: Context) {

        val logs = sleepGoalDAO.findByUserId(ctx.pathParam("user-id").toInt())
        if (logs.isNotEmpty()) {
            ctx.json(logs)
            ctx.status(200)
        } else {
            ctx.status(404)
        }

    }

    fun addSleepGoal(ctx: Context) {
        val sleep_goal: SleepGoal = jsonToObject(ctx.body())
        val sleepGoalId = sleepGoalDAO.save(sleep_goal)
        if (sleepGoalId != null) {
            sleep_goal.id = sleepGoalId
            ctx.json(
                mapOf(
                    "id" to sleep_goal.id,
                    "userId" to sleep_goal.userId,
                    "targetSleepHours" to sleep_goal.targetSleepHours,
                    "targetSleepQuality" to sleep_goal.targetSleepQuality,
                    "targetSleepTiming" to sleep_goal.targetSleepTiming
                    )
            )
            ctx.status(201)
        }
    }

    fun updateSleepGoalByGoalId(ctx: Context){
        val sleepgoal : SleepGoal = jsonToObject(ctx.body())
        if (sleepGoalDAO.updateBySleepGoalId(
                sleepGoalId = ctx.pathParam("sleep-goal-id").toInt(),
                sleepGoalToUpdate = sleepgoal) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }


    //Sleep Log and Stats Controllers


    fun addSleepLogAndStat(ctx: Context) {
        val sleep_log_and_performance: SleepGoalLogAndStat = jsonToObject(ctx.body())
        val sleeplogId = sleepGoalLogAndStatDAO.save(sleep_log_and_performance)
        if (sleeplogId != null) {
            sleep_log_and_performance.id = sleeplogId
            ctx.json(
                mapOf(
                    "id" to sleep_log_and_performance.id,
                    "userId" to sleep_log_and_performance.userId,
                    "sleepGoalId" to sleep_log_and_performance.sleepGoalId,
                    "actualSleepHours" to sleep_log_and_performance.actualSleepHours,
                    "actualSleepQuality" to sleep_log_and_performance.actualSleepQuality,
                    "actualSleepTiming" to sleep_log_and_performance.actualSleepTiming,
                )
            )
            ctx.status(201)
        }
        else{
            ctx.status(500)
        }
    }



    fun getSleepLogAndStatByUserId(ctx: Context) {

        val stats = sleepGoalLogAndStatDAO.findByUserId(ctx.pathParam("user-id").toInt())
        if (stats.isNotEmpty()) {
            ctx.json(stats)
            ctx.status(200)
        } else {
            ctx.status(404)
        }

    }

    fun deleteByUserId(ctx: Context){
        if (sleepGoalLogAndStatDAO.deleteBySleepGoalId(ctx.pathParam("sleep_goal_id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }


}


