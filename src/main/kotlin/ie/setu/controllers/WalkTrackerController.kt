package ie.setu.controllers

import ie.setu.domain.walk.WalkGoal
import ie.setu.domain.repository.walk.WalkGoalDAO
import ie.setu.domain.repository.walk.WalkGoalLogAndStatsDAO
import ie.setu.domain.walk.WalkLogAndStat
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object WalkTrackerController {
    private val walkGoalDAO = WalkGoalDAO()
    private val walkGoalLogAndStatDAO = WalkGoalLogAndStatsDAO()

    // Walk Goal Controllers

    fun getAllWalkGoals(ctx: Context) {
        val goals = walkGoalDAO.getAll()
        if (goals.isNotEmpty()) {
            ctx.json(goals)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun getWalkGoalByUserId(ctx: Context) {

        val logs = walkGoalDAO.findByUserId(ctx.pathParam("user-id").toInt())
        if (logs.isNotEmpty()) {
            ctx.json(logs)
            ctx.status(200)
        } else {
            ctx.status(404)
        }

    }

    fun addWalkGoal(ctx: Context) {
        val walkGoal: WalkGoal = jsonToObject(ctx.body())
        val walkGoalId = walkGoalDAO.save(walkGoal)
        if (walkGoalId != null) {
            walkGoal.id = walkGoalId
            ctx.json(
                mapOf(
                    "id" to walkGoal.id,
                    "userId" to walkGoal.userId,
                    "targetSteps" to walkGoal.targetSteps,
                    "uphill" to walkGoal.uphill
                )
            )
            ctx.status(201)
        }
    }

    fun updateWalkGoalByGoalId(ctx: Context) {
        val walkGoal: WalkGoal = jsonToObject(ctx.body())
        println("WalkGoalId: $walkGoal")

        if (walkGoalDAO.updateByWalkGoalId(
                walkGoalId = ctx.pathParam("walk-goal-id").toInt(),
                walkGoalToUpdate = walkGoal) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    // Walk Log and Stats Controllers

    fun addWalkLogAndPerformance(ctx: Context) {
        val walkLogAndPerformance: WalkLogAndStat = jsonToObject(ctx.body())
        val walkLogId = walkGoalLogAndStatDAO.save(walkLogAndPerformance)
        if (walkLogId != null) {
            walkLogAndPerformance.id = walkLogId
            ctx.json(
                mapOf(
                    "id" to walkLogAndPerformance.id,
                    "userId" to walkLogAndPerformance.userId,
                    "walkGoalId" to walkLogAndPerformance.walkGoalId,
                    "actualSteps" to walkLogAndPerformance.actualSteps,
                )
            )
            ctx.status(201)
        } else {
            ctx.status(500)
        }
    }

    fun getWalkPerformanceByUserId(ctx: Context) {
        val performance = walkGoalLogAndStatDAO.findByUserId(ctx.pathParam("user-id").toInt())
        if (performance.isNotEmpty()) {
            ctx.json(performance)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun deleteByWalkGoalId(ctx: Context) {
        if (walkGoalLogAndStatDAO.deleteByWalkGoalId(ctx.pathParam("walk_goal_id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}
