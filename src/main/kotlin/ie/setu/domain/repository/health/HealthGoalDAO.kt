
package ie.setu.domain.repository.health

import ie.setu.domain.health.HealthGoal
import ie.setu.domain.db.health.HealthGoals
import ie.setu.utils.mapToHealthGoal
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime


class HealthGoalDAO {


    fun getAll():  ArrayList<HealthGoal> {
        val healthGoalList: ArrayList<HealthGoal> = arrayListOf()

        transaction {
            HealthGoals.selectAll().map {
                healthGoalList.add(mapToHealthGoal(it))
            }
        }
        return healthGoalList
    }




    fun save(health_goal: HealthGoal) : Int?{
        return transaction {
            val healthlogId=HealthGoals.insert {
                it[userId] = health_goal.userId
                it[healthGoalType] = health_goal.healthGoalType
                it[targetValue] = health_goal.targetValue
                it[entryTime] = DateTime.now()
            } get HealthGoals.id
            healthlogId
        }
    }

    fun updateByHealthGoalId(healthGoalId: Int,healthGoalToUpdate: HealthGoal) : Int?{
        return transaction {
            HealthGoals.update ({
                HealthGoals.id eq healthGoalId}){
                it[userId] = healthGoalToUpdate.userId
                it[healthGoalType] = healthGoalToUpdate.healthGoalType
                it[targetValue] = healthGoalToUpdate.targetValue
                it[entryTime] = DateTime.now()
            }
        }
    }
}