
package ie.setu.domain.repository.diet

import ie.setu.domain.diet.DietGoal
import ie.setu.domain.db.diet.DietGoals
import ie.setu.domain.db.health.HealthGoals
import ie.setu.domain.health.HealthGoal
import ie.setu.utils.mapToDietGoal
import ie.setu.utils.mapToHealthGoal
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime


class DietGoalDAO {


    fun getAll():  ArrayList<DietGoal> {
        val dietGoalList: ArrayList<DietGoal> = arrayListOf()

        transaction {
            DietGoals.selectAll().map {
                dietGoalList.add(mapToDietGoal(it))
            }
        }
        return dietGoalList
    }

    fun findByUserId(userId: Int): List<DietGoal>{
        return transaction {
            DietGoals
                .selectAll().where { DietGoals.userId eq userId}
                .map { mapToDietGoal(it) }
        }
    }

    fun save(diet_goal: DietGoal) : Int?{
        return transaction {
            val dietlogId=DietGoals.insert {
                it[userId] = diet_goal.userId
                it[dietGoalType] = diet_goal.dietGoalType
                it[targetCalories ] = diet_goal.targetCalories
                it[entryTime] = DateTime.now()
            } get DietGoals.id
            dietlogId
        }
    }

    fun updateByDietGoalId(dietGoalId: Int,dietGoalToUpdate: DietGoal) : Int?{
        return transaction {
            DietGoals.update ({
                DietGoals.id eq dietGoalId}){
                it[userId] = dietGoalToUpdate.userId
                it[dietGoalType] = dietGoalToUpdate.dietGoalType
                it[targetCalories] = dietGoalToUpdate.targetCalories
                it[entryTime] = DateTime.now()
            }
        }
    }
}