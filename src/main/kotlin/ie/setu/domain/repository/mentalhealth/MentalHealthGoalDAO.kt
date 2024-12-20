package ie.setu.domain.repository.mentalhealth

import ie.setu.domain.db.mentalhealth.MentalHealthGoals
import ie.setu.domain.db.water.WaterGoals
import ie.setu.domain.mentalhealth.MentalHealthGoal
import ie.setu.domain.water.WaterGoal
import ie.setu.utils.mapToMentalHealthGoal
import ie.setu.utils.mapToWaterGoal
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime

class MentalHealthGoalDAO {

    fun getAll(): ArrayList<MentalHealthGoal> {
        val mentalHealthGoalList: ArrayList<MentalHealthGoal> = arrayListOf()

        transaction {
            MentalHealthGoals.selectAll().map {
                mentalHealthGoalList.add(mapToMentalHealthGoal(it))
            }
        }
        return mentalHealthGoalList
    }

    fun findByUserId(userId: Int): List<MentalHealthGoal>{
        return transaction {
            MentalHealthGoals
                .selectAll().where { MentalHealthGoals.userId eq userId}
                .map { mapToMentalHealthGoal(it) }
        }
    }


    fun save(mentalHealthGoal: MentalHealthGoal): Int? {
        return transaction {
            val mentalHealthGoalId = MentalHealthGoals.insert {
                it[userId] = mentalHealthGoal.userId
                it[targetMoodScore] = mentalHealthGoal.targetMoodScore
                it[entryTime] = DateTime.now()
            } get MentalHealthGoals.id
            mentalHealthGoalId
        }
    }

    fun updateByMentalHealthGoalId(mentalHealthGoalId: Int, mentalHealthGoalToUpdate: MentalHealthGoal): Int? {
        return transaction {
            MentalHealthGoals.update({
                MentalHealthGoals.id eq mentalHealthGoalId
            }) {
                it[userId] = mentalHealthGoalToUpdate.userId
                it[targetMoodScore] = mentalHealthGoalToUpdate.targetMoodScore
                it[entryTime] = DateTime.now()
            }
        }
    }
}
