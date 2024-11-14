package ie.setu.controllers

import ie.setu.domain.Activity
import ie.setu.domain.diet.DietGoal
import ie.setu.domain.diet.DietGoalLogAndPerformance

import ie.setu.domain.repository.diet.DietGoalDAO
import ie.setu.domain.repository.diet.DietGoalLogAndPerformanceDAO
import ie.setu.utils.jsonObjectMapper
import ie.setu.utils.jsonToObject
import io.github.oshai.kotlinlogging.KotlinLogging
import io.javalin.http.Context


object DietGoalTrackerController {
    private val dietGoalDAO = DietGoalDAO()
    private val dietGoalLogAndPerformanceDAO = DietGoalLogAndPerformanceDAO()

    //Diet Goal Controllers

    fun getAllDietGoal(ctx: Context) {
        val goal = dietGoalDAO.getAll()
        if (goal.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(goal)
    }


    fun addDietGoal(ctx: Context) {
        val diet_goal: DietGoal = jsonToObject(ctx.body())
        val dietGoalId = dietGoalDAO.save(diet_goal)
        if (dietGoalId != null) {
            diet_goal.id = dietGoalId
            ctx.json(
                mapOf(
                    "id" to diet_goal.id,
                    "userId" to diet_goal.userId,
                    "dietGoalType " to diet_goal.dietGoalType,
                    "targetCalories " to diet_goal.targetCalories,
                )
            )
            ctx.status(201)
        }
    }

    fun updateDietGoalByGoalId(ctx: Context){
        val dietgoal : DietGoal = jsonToObject(ctx.body())
        if (dietGoalDAO.updateByDietGoalId(
                dietGoalId = ctx.pathParam("diet-goal-id").toInt(),
                dietGoalToUpdate = dietgoal) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }


    //Health Log and Performance Controllers


    fun addDietLogAndPerformance(ctx: Context) {
        val diet_log_and_performance: DietGoalLogAndPerformance = jsonToObject(ctx.body())
        val healthlogId = dietGoalLogAndPerformanceDAO.save(diet_log_and_performance)
        if (healthlogId != null) {
            diet_log_and_performance.id = healthlogId
            ctx.json(
                mapOf(
                    "id" to diet_log_and_performance.id,
                    "userId" to diet_log_and_performance.userId,
                    "dietGoalId" to diet_log_and_performance.dietGoalId,
                    "caloriesConsumed" to diet_log_and_performance.caloriesConsumed
                )
            )
            ctx.status(201)
        }
        else{
            ctx.status(500)
        }
    }



    fun getDietPerformanceByUserId(ctx: Context) {

        val performance = dietGoalLogAndPerformanceDAO.findByUserId(ctx.pathParam("user-id").toInt())
        if (performance.isNotEmpty()) {
            ctx.json(performance)
            ctx.status(200)
        } else {
            ctx.status(404)
        }

    }

    fun deleteByUserId(ctx: Context){
        if (dietGoalLogAndPerformanceDAO.deleteByDietGoalId(ctx.pathParam("diet_goal_id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }


}


