package ie.setu.domain.repository.screentime

import ie.setu.domain.screentime.ScreenTimeLogAndPerformance
import ie.setu.domain.db.screentime.ScreenTimeLogAndPerformances
import ie.setu.domain.db.screentime.ScreenTimeGoals
import ie.setu.utils.mapToScreenTimeLogAndPerformance
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.joda.time.DateTime


class ScreenTimeLogAndPerformanceDAO {

    fun save(screenTimeGoalLogAndPerformance: ScreenTimeLogAndPerformance): Int? {
        return transaction {
            val screenTimeGoal = ScreenTimeGoals
                .selectAll()
                .firstOrNull { it[ScreenTimeGoals.userId] == screenTimeGoalLogAndPerformance.userId }

            if (screenTimeGoal != null) {
                val targetScreenTimeValue = screenTimeGoal[ScreenTimeGoals.targetScreenHours]

                val targetMetValue  = screenTimeGoalLogAndPerformance.actualScreenHours <= targetScreenTimeValue
                val extraHoursValue= if (screenTimeGoalLogAndPerformance.actualScreenHours > targetScreenTimeValue) (screenTimeGoalLogAndPerformance.actualScreenHours - targetScreenTimeValue) else 0
                val recommendationsValue= if (targetMetValue) "You have done it, keep it up" else "Try to adopt habits that help you to decrease screen hours"

                val screenTimeGoalLogId = ScreenTimeLogAndPerformances.insert {
                    it[userId] = screenTimeGoalLogAndPerformance.userId
                    it[screenTimeGoalId] = screenTimeGoalLogAndPerformance.screenTimeGoalId
                    it[actualScreenHours ] = screenTimeGoalLogAndPerformance.actualScreenHours
                    it[targetMet ] = targetMetValue
                    it[extraHours ] = extraHoursValue
                    it[recommendations ] = recommendationsValue
                    it[entryTime] = DateTime.now()
                } get ScreenTimeLogAndPerformances.id

                screenTimeGoalLogId
            } else {
                null
            }
        }
    }


    fun findByUserId(userId: Int): List<ScreenTimeLogAndPerformance>{
        return transaction {
            ScreenTimeLogAndPerformances
                .selectAll().where { ScreenTimeLogAndPerformances.userId eq userId}
                .map { mapToScreenTimeLogAndPerformance(it) }
        }
    }

    fun deleteByScreenTimeGoalId (screenTimeGoalId: Int): Int{
        return transaction{
            ScreenTimeLogAndPerformances.deleteWhere { ScreenTimeLogAndPerformances.screenTimeGoalId eq screenTimeGoalId }
        }
    }



}

