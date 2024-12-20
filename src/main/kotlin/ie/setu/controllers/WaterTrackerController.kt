package ie.setu.controllers

import ie.setu.domain.water.WaterGoal
import ie.setu.domain.repository.water.WaterGoalDAO
import ie.setu.domain.repository.water.WaterGoalLogAndStatsDAO
import ie.setu.domain.water.WaterLogAndStat
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object WaterTrackerController {
    private val waterGoalDAO = WaterGoalDAO()
    private val waterGoalLogAndStatDAO = WaterGoalLogAndStatsDAO()

    //Water Goal Controllers

    fun getAllWaterGoal(ctx: Context) {
        val goal = waterGoalDAO.getAll()
        if (goal.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(goal)
    }

    fun getWaterGoalByUserId(ctx: Context) {

        val logs = waterGoalDAO.findByUserId(ctx.pathParam("user-id").toInt())
        if (logs.isNotEmpty()) {
            ctx.json(logs)
            ctx.status(200)
        } else {
            ctx.status(404)
        }

    }

    fun addWaterGoal(ctx: Context) {
        val water_goal: WaterGoal = jsonToObject(ctx.body())
        val waterGoalId = waterGoalDAO.save(water_goal)
        if (waterGoalId != null) {
            water_goal.id = waterGoalId
            ctx.json(
                mapOf(
                    "id" to water_goal.id,
                    "userId" to water_goal.userId,
                    "waterTarget" to water_goal.waterTarget,
                )
            )
            ctx.status(201)
        }
    }

    fun updateWaterGoalByGoalId(ctx: Context){
        val watergoal : WaterGoal = jsonToObject(ctx.body())
        println("WaterGoalId: $watergoal")

        if (waterGoalDAO.updateByWaterGoalId(

                        waterGoalId = ctx.pathParam("water-goal-id").toInt(),
                waterGoalToUpdate = watergoal) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }


    //Water Log and Performance Controllers


    fun addWaterLogAndPerformance(ctx: Context) {
        val water_log_and_performance: WaterLogAndStat = jsonToObject(ctx.body())
        val waterlogId = waterGoalLogAndStatDAO.save(water_log_and_performance)
        if (waterlogId != null) {
            water_log_and_performance.id = waterlogId
            ctx.json(
                mapOf(
                    "id" to water_log_and_performance.id,
                    "userId" to water_log_and_performance.userId,
                    "waterGoalId" to water_log_and_performance.waterGoalId,
                    "actualWaterIntake " to water_log_and_performance.actualWaterIntake
                )
            )
            ctx.status(201)
        }
        else{
            ctx.status(500)
        }
    }



    fun getWaterPerformanceByUserId(ctx: Context) {

        val performance = waterGoalLogAndStatDAO.findByUserId(ctx.pathParam("user-id").toInt())
        if (performance.isNotEmpty()) {
            ctx.json(performance)
            ctx.status(200)
        } else {
            ctx.status(404)
        }

    }

    fun deleteByWaterGoalId(ctx: Context){
        if (waterGoalLogAndStatDAO.deleteByWaterGoalId(ctx.pathParam("water_goal_id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }


}


