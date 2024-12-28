package ie.setu.controllers

import ie.setu.domain.health.HealthGoal
import ie.setu.domain.health.HealthGoalLogAndPerformance
import ie.setu.domain.repository.health.HealthGoalDAO
import ie.setu.domain.repository.health.HealthGoalLogAndPerformanceDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context
import org.joda.time.DateTime

object HealthTrackerController {
    private val healthGoalDAO = HealthGoalDAO()
    private val healthlogAndPerformanceDAO = HealthGoalLogAndPerformanceDAO()

    //Health Goal Controllers

    fun getAllHealthGoal(ctx: Context) {
        val goal = healthGoalDAO.getAll()
        if (goal.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(goal)
    }

    fun getHealthGoalsByUserId(ctx: Context) {

        val logs = healthGoalDAO.findByUserId(ctx.pathParam("user-id").toInt())
        if (logs.isNotEmpty()) {
            ctx.json(logs)
            ctx.status(200)
        } else {
            ctx.status(404)
        }

    }
    fun addHealthGoal(ctx: Context) {
        val health_goal: HealthGoal = jsonToObject(ctx.body())
        val healthGoalId = healthGoalDAO.save(health_goal)
        if (healthGoalId != null) {
            health_goal.id = healthGoalId
            ctx.json(
                mapOf(
                    "id" to health_goal.id,
                    "userId" to health_goal.userId,
                    "healthGoalType" to health_goal.healthGoalType,
                    "targetValue" to health_goal.targetValue,
                    "entryTime" to DateTime.now()
                )
            )
            ctx.status(201)
        }
    }

        fun updateHealthGoalByGoalId(ctx: Context){
        val healthgoal : HealthGoal = jsonToObject(ctx.body())
        if (healthGoalDAO.updateByHealthGoalId(
                healthGoalId = ctx.pathParam("health-goal-id").toInt(),
                healthGoalToUpdate = healthgoal) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }


    //Health Log and Performance Controllers


    fun addHealthLogAndPerformance(ctx: Context) {
        val health_log_and_performance: HealthGoalLogAndPerformance = jsonToObject(ctx.body())
        val healthlogId = healthlogAndPerformanceDAO.save(health_log_and_performance)
        if (healthlogId != null) {
            health_log_and_performance.id = healthlogId
            ctx.json(
                mapOf(
                    "id" to health_log_and_performance.id,
                    "userId" to health_log_and_performance.userId,
                    "healthGoalId" to health_log_and_performance.healthGoalId,
                    "achievedValue" to health_log_and_performance.achievedValue
                )
            )
            ctx.status(201)
        }
        else{
            ctx.status(500)
        }
    }



    fun getHealthPerformanceByUserId(ctx: Context) {

        val performance = healthlogAndPerformanceDAO.findByUserId(ctx.pathParam("user-id").toInt())
        if (performance.isNotEmpty()) {
            ctx.json(performance)
            ctx.status(200)
        } else {
            ctx.status(404)
        }

    }

    fun deleteByUserId(ctx: Context){
        if (healthlogAndPerformanceDAO.deleteByHealthGoalId(ctx.pathParam("health_goal_id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }


}


