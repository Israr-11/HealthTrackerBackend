package ie.setu.domain.repository.diet

import ie.setu.domain.diet.DietGoalLogAndPerformance
import ie.setu.domain.db.diet.DietGoalLogAndPerformances
import ie.setu.domain.db.diet.DietGoals
import ie.setu.utils.mapToDietGoalLogAndPerformance
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.joda.time.DateTime


class DietGoalLogAndPerformanceDAO {

    fun save(dietGoalLogAndPerformance: DietGoalLogAndPerformance): Int? {
        return transaction {
            val dietGoal = DietGoals
                .selectAll()
                .firstOrNull { it[DietGoals.userId] == dietGoalLogAndPerformance.userId }
           println(dietGoal)
            if (dietGoal != null) {
                val targetCaloriesValue = dietGoal[DietGoals.targetCalories]

                val targetCaloriesReachedValue = dietGoalLogAndPerformance.caloriesConsumed >= targetCaloriesValue
                val surplusDeficitValue = if (targetCaloriesReachedValue) "deficit" else "surplus"
                val statusRecieved= if (targetCaloriesReachedValue) "completed" else "not_completed"
                val remarksValue = if (targetCaloriesReachedValue) "Target achieved, well done" else "Target not achieved,please focus on achieving set targets"

                val dietGoalLogId = DietGoalLogAndPerformances.insert {
                    it[userId] = dietGoalLogAndPerformance.userId
                    it[dietGoalId] = dietGoalLogAndPerformance.dietGoalId
                    it[status] = statusRecieved
                    it[caloriesConsumed] = dietGoalLogAndPerformance.caloriesConsumed
                    it[deficitSurplus] = surplusDeficitValue
                    it[recommendations] = remarksValue
                    it[targetReached]=targetCaloriesReachedValue
                    it[entryTime] = DateTime.now()
                } get DietGoalLogAndPerformances.id

                dietGoalLogId
            } else {
                null
            }
        }
    }


    fun findByUserId(userId: Int): List<DietGoalLogAndPerformance>{
        return transaction {
            DietGoalLogAndPerformances
                .selectAll().where { DietGoalLogAndPerformances.userId eq userId}
                .map { mapToDietGoalLogAndPerformance(it) }
        }
    }

    fun deleteByDietGoalId (dietGoalId: Int): Int{
        return transaction{
            DietGoalLogAndPerformances.deleteWhere { DietGoalLogAndPerformances.dietGoalId eq dietGoalId }
        }
    }



}

