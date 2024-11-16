package ie.setu.domain.repository.health

import ie.setu.domain.health.HealthGoalLogAndPerformance
import ie.setu.domain.db.health.HealthGoalLogAndPerformances
import ie.setu.domain.db.health.HealthGoals
import ie.setu.utils.mapToHealthGoalLogAndPerformance
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.joda.time.DateTime


class HealthGoalLogAndPerformanceDAO {

    fun save(healthGoalLogAndPerformance: HealthGoalLogAndPerformance): Int? {
        return transaction {
            val healthGoal = HealthGoals
                .selectAll()
                .firstOrNull { it[HealthGoals.userId] == healthGoalLogAndPerformance.userId }

            if (healthGoal != null) {
                val targetValue = healthGoal[HealthGoals.targetValue]

                val targetReachedValue = healthGoalLogAndPerformance.achievedValue >= targetValue
                val statusRecieved= if (targetReachedValue) "completed" else "not_completed"
                val remarksValue = if (targetReachedValue) "Target achieved, well done" else "Target not achieved,please focus on achieving set targets"

                val healthGoalLogId = HealthGoalLogAndPerformances.insert {
                    it[userId] = healthGoalLogAndPerformance.userId
                    it[healthGoalId] = healthGoalLogAndPerformance.healthGoalId
                    it[status] = statusRecieved
                    it[achievedValue] = healthGoalLogAndPerformance.achievedValue
                    it[targetReached] = targetReachedValue
                    it[remarks] = remarksValue
                    it[entryTime] = DateTime.now()
                } get HealthGoalLogAndPerformances.id

                healthGoalLogId
            } else {
                null
            }
        }
    }


    fun findByUserId(userId: Int): List<HealthGoalLogAndPerformance>{
        return transaction {
            HealthGoalLogAndPerformances
                .selectAll().where { HealthGoalLogAndPerformances.userId eq userId}
                .map { mapToHealthGoalLogAndPerformance(it) }
        }
    }

    fun deleteByHealthGoalId (healthGoalId: Int): Int{
        return transaction{
            HealthGoalLogAndPerformances.deleteWhere { HealthGoalLogAndPerformances.healthGoalId eq healthGoalId }
        }
    }



}

