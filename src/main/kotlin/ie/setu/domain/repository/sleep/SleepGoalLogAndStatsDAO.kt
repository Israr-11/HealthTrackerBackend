package ie.setu.domain.repository.sleep

import ie.setu.domain.sleep.SleepGoalLogAndStat
import ie.setu.domain.db.sleep.SleepGoalLogAndStats
import ie.setu.domain.db.sleep.SleepGoals
import ie.setu.utils.mapToSleepGoalLogAndStat
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.joda.time.DateTime


class SleepGoalLogAndStatsDAO {

    fun save(sleepGoalLogAndStat: SleepGoalLogAndStat): Int? {
        return transaction {
            val sleepGoal = SleepGoals
                .selectAll()
                .firstOrNull { it[SleepGoals.userId] == sleepGoalLogAndStat.userId }

            if (sleepGoal != null) {
                val targetSleepHoursValue = sleepGoal[SleepGoals.targetSleepHours ]

                val actualSleepHoursValue=sleepGoalLogAndStat.actualSleepHours
                val actualSleepQualityValue=sleepGoalLogAndStat.actualSleepQuality

                val sleepInterruptionsValue = when {
                    actualSleepHoursValue < targetSleepHoursValue -> (targetSleepHoursValue - actualSleepHoursValue)
                    actualSleepHoursValue == targetSleepHoursValue -> 0
                    else -> (actualSleepHoursValue - targetSleepHoursValue)
                }
                val sleepPhaseDetailsValue = when (actualSleepQualityValue) {
                    "good" -> "80% Deep Sleep, 15% Light Sleep, 5% REM Sleep"
                    "average" -> "60% Deep Sleep, 30% Light Sleep, 10% REM Sleep"
                    "poor" -> "40% Deep Sleep, 40% Light Sleep, 20% REM Sleep"
                    else -> "Data unavailable"
                }

                val sleepQualityIndexValue = when {
                    actualSleepHoursValue >= targetSleepHoursValue && actualSleepQualityValue == "good" -> 100
                    actualSleepHoursValue >= targetSleepHoursValue -> 80
                    else -> 50
                }
                val remarksValue = when {
                    actualSleepHoursValue >= targetSleepHoursValue && actualSleepQualityValue == "good" -> "Great sleep quality and duration!"
                    actualSleepHoursValue >= targetSleepHoursValue -> "Target sleep hours met, but quality needs improvement."
                    else -> "You need to improve both sleep duration and quality."
                }
                val sleepGoalLogId = SleepGoalLogAndStats.insert {
                    it[userId] = sleepGoalLogAndStat.userId
                    it[sleepGoalId] = sleepGoalLogAndStat.sleepGoalId
                    it[actualSleepHours] = sleepGoalLogAndStat.actualSleepHours
                    it[actualSleepQuality] = sleepGoalLogAndStat.actualSleepQuality
                    it[actualSleepTiming] = sleepGoalLogAndStat.actualSleepTiming
                    it[sleepInterruptions] = sleepInterruptionsValue
                    it[sleepPhaseDetails]= sleepPhaseDetailsValue
                    it[sleepQualityIndex]= sleepQualityIndexValue
                    it[remarks]= remarksValue
                    it[entryTime] = DateTime.now()
                } get SleepGoalLogAndStats.id

                sleepGoalLogId
            } else {
                null
            }
        }
    }


    fun findByUserId(userId: Int): List<SleepGoalLogAndStat>{
        return transaction {
            SleepGoalLogAndStats
                .selectAll().where { SleepGoalLogAndStats.userId eq userId}
                .map { mapToSleepGoalLogAndStat(it) }
        }
    }

    fun deleteBySleepGoalId (sleepGoalId: Int): Int{
        return transaction{
            SleepGoalLogAndStats.deleteWhere { SleepGoalLogAndStats.sleepGoalId eq sleepGoalId }
        }
    }



}

