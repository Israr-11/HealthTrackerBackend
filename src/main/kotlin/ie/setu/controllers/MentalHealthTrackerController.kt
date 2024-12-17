package ie.setu.controllers

import ie.setu.domain.mentalhealth.MentalHealthGoal
import ie.setu.domain.repository.mentalhealth.MentalHealthGoalDAO
import ie.setu.domain.repository.mentalhealth.MentalHealthLogAndPerformanceDAO
import ie.setu.domain.mentalhealth.MentalHealthLogAndPerformance
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object MentalHealthTrackerController {
    private val mentalHealthGoalDAO = MentalHealthGoalDAO()
    private val mentalHealthLogAndPerformanceDAO = MentalHealthLogAndPerformanceDAO()

    // Mental Health Goal Controllers

    fun getAllMentalHealthGoals(ctx: Context) {
        val goals = mentalHealthGoalDAO.getAll()
        if (goals.isNotEmpty()) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(goals)
    }

    fun addMentalHealthGoal(ctx: Context) {
        val mental_health_goal: MentalHealthGoal = jsonToObject(ctx.body())
        val mentalHealthGoalId = mentalHealthGoalDAO.save(mental_health_goal)
        if (mentalHealthGoalId != null) {
            mental_health_goal.id = mentalHealthGoalId
            ctx.json(
                mapOf(
                    "id" to mental_health_goal.id,
                    "userId" to mental_health_goal.userId,
                    "targetMoodScore" to mental_health_goal.targetMoodScore,
                )
            )
            ctx.status(201)
        }
    }

    fun updateMentalHealthGoalByGoalId(ctx: Context) {
        val mentalHealthGoal: MentalHealthGoal = jsonToObject(ctx.body())
        if (mentalHealthGoalDAO.updateByMentalHealthGoalId(
                mentalHealthGoalId = ctx.pathParam("mental-health-goal-id").toInt(),
                mentalHealthGoalToUpdate = mentalHealthGoal) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    // Mental Health Log and Performance Controllers

    fun addMentalHealthLogAndPerformance(ctx: Context) {
        val mental_health_log_and_performance: MentalHealthLogAndPerformance = jsonToObject(ctx.body())
        val mentalHealthLogId = mentalHealthLogAndPerformanceDAO.save(mental_health_log_and_performance)
        if (mentalHealthLogId != null) {
            mental_health_log_and_performance.id = mentalHealthLogId
            ctx.json(
                mapOf(
                    "id" to mental_health_log_and_performance.id,
                    "userId" to mental_health_log_and_performance.userId,
                    "mentalHealthGoalId" to mental_health_log_and_performance.mentalHealthGoalId,
                    "moodScore" to mental_health_log_and_performance.moodScore
                )
            )
            ctx.status(201)
        } else {
            ctx.status(500)
        }
    }

    fun getMentalHealthPerformanceByUserId(ctx: Context) {
        val performance = mentalHealthLogAndPerformanceDAO.findByUserId(ctx.pathParam("user-id").toInt())
        if (performance.isNotEmpty()) {
            ctx.json(performance)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun deleteByMentalHealthGoalId(ctx: Context) {
        if (mentalHealthLogAndPerformanceDAO.deleteByMentalHealthGoalId(ctx.pathParam("mental_health_goal_id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}
