package ie.setu.domain.repository.mentalhealth

import ie.setu.domain.mentalhealth.MentalHealthLogAndPerformance
import ie.setu.domain.db.mentalhealth.MentalHealthLogAndPerformances
import ie.setu.domain.db.mentalhealth.MentalHealthGoals
import ie.setu.utils.mapToMentalHealthLogAndPerformance
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.joda.time.DateTime

class MentalHealthLogAndPerformanceDAO {

    fun save(mentalHealthLogAndPerformance: MentalHealthLogAndPerformance): Int? {
        return transaction {
            val mentalHealthGoal = MentalHealthGoals
                .selectAll()
                .firstOrNull { it[MentalHealthGoals.userId] == mentalHealthLogAndPerformance.userId }

            if (mentalHealthGoal != null) {
                val targetMoodScoreValue = mentalHealthGoal[MentalHealthGoals.targetMoodScore]

                val targetMetValue = mentalHealthLogAndPerformance.moodScore >= targetMoodScoreValue
                val recommendationsValue = if (targetMetValue) {
                    "Great job achieving your target mood! Keep maintaining positive habits."
                } else {
                    "Consider practices like mindfulness or relaxation to improve your mood."
                }
                val stressLevelValue = if (mentalHealthLogAndPerformance.moodScore >= 8) 1 else if (mentalHealthLogAndPerformance.moodScore in 5..7) 5 else 9


                val mentalHealthLogId = MentalHealthLogAndPerformances.insert {
                    it[userId] = mentalHealthLogAndPerformance.userId
                    it[mentalHealthGoalId] = mentalHealthLogAndPerformance.mentalHealthGoalId
                    it[moodScore] = mentalHealthLogAndPerformance.moodScore
                    it[stressLevel] = stressLevelValue
                    it[targetMet] = targetMetValue
                    it[recommendations] = recommendationsValue
                    it[entryTime] = DateTime.now()
                } get MentalHealthLogAndPerformances.id

                mentalHealthLogId
            } else {
                null
            }
        }
    }

    fun findByUserId(userId: Int): List<MentalHealthLogAndPerformance> {
        return transaction {
            MentalHealthLogAndPerformances
                .selectAll().where { MentalHealthLogAndPerformances.userId eq userId }
                .map { mapToMentalHealthLogAndPerformance(it) }
        }
    }

    fun deleteByMentalHealthGoalId(mentalHealthGoalId: Int): Int {
        return transaction {
            MentalHealthLogAndPerformances.deleteWhere { MentalHealthLogAndPerformances.mentalHealthGoalId eq mentalHealthGoalId }
        }
    }
}
