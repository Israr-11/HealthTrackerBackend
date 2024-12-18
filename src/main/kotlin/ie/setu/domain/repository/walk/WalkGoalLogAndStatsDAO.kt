package ie.setu.domain.repository.walk

import ie.setu.domain.db.walk.WalkGoalLogAndStats
import ie.setu.domain.db.walk.WalkGoals
import ie.setu.domain.walk.WalkLogAndStat
import ie.setu.utils.mapToWalkLogAndStat
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.joda.time.DateTime

class WalkGoalLogAndStatsDAO {

    fun save(walkGoalLogAndStat: WalkLogAndStat): Int? {
        return transaction {
            val walkGoal = WalkGoals
                .selectAll()
                .firstOrNull { it[WalkGoals.userId] == walkGoalLogAndStat.userId }

            if (walkGoal != null) {
                val dailyStepTargetValue = walkGoal[WalkGoals.targetSteps]
                val uphillValue = walkGoal[WalkGoals.uphill]

                val targetMetValue = walkGoalLogAndStat.actualSteps >= dailyStepTargetValue

                val extraStepsValue = if (walkGoalLogAndStat.actualSteps > dailyStepTargetValue) {
                    walkGoalLogAndStat.actualSteps - dailyStepTargetValue
                } else {
                    0
                }

                val walkQualityValue = if (uphillValue && extraStepsValue > 0) {
                    "High"
                } else {
                    "Normal"
                }

                val recommendationsValue = if (targetMetValue) {
                    "Great job! You've met your target!"
                } else {
                    "Keep going! Aim to meet your target steps."
                }

                val walkGoalLogId = WalkGoalLogAndStats.insert {
                    it[userId] = walkGoalLogAndStat.userId
                    it[walkGoalId] = walkGoalLogAndStat.walkGoalId
                    it[actualSteps] = walkGoalLogAndStat.actualSteps
                    it[targetMet] = targetMetValue
                    it[extraSteps] = extraStepsValue
                    it[walkQuality] = walkQualityValue
                    it[recommendations] = recommendationsValue
                    it[entryTime] = DateTime.now()
                } get WalkGoalLogAndStats.id

                walkGoalLogId
            } else {
                null
            }
        }
    }

    fun findByUserId(userId: Int): List<WalkLogAndStat> {
        return transaction {
            WalkGoalLogAndStats
                .selectAll().where { WalkGoalLogAndStats.userId eq userId }
                .map { mapToWalkLogAndStat(it) }
        }
    }

    fun deleteByWalkGoalId(walkGoalId: Int): Int {
        return transaction {
            WalkGoalLogAndStats.deleteWhere { WalkGoalLogAndStats.walkGoalId eq walkGoalId }
        }
    }
}
