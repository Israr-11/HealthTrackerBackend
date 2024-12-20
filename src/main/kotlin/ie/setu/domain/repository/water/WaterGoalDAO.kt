package ie.setu.domain.repository.water

import ie.setu.domain.db.sleep.SleepGoals
import ie.setu.domain.db.water.WaterGoals
import ie.setu.domain.sleep.SleepGoal
import ie.setu.domain.water.WaterGoal
import ie.setu.utils.mapToSleepGoal
import ie.setu.utils.mapToWaterGoal
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime


class WaterGoalDAO {


    fun getAll():  ArrayList<WaterGoal> {
        val WaterGoalsList: ArrayList<WaterGoal> = arrayListOf()

        transaction {
            WaterGoals.selectAll().map {
                WaterGoalsList.add(mapToWaterGoal(it))
            }
        }
        return WaterGoalsList
    }

    fun findByUserId(userId: Int): List<WaterGoal>{
        return transaction {
            WaterGoals
                .selectAll().where { WaterGoals.userId eq userId}
                .map { mapToWaterGoal(it) }
        }
    }


    fun save(water_goal: WaterGoal) : Int?{
        return transaction {
            val waterlogId=WaterGoals.insert {
                it[userId] = water_goal.userId
                it[waterTarget ] = water_goal.waterTarget
                it[entryTime] = DateTime.now()
            } get WaterGoals.id
            waterlogId
        }
    }

    fun updateByWaterGoalId(waterGoalId: Int,waterGoalToUpdate: WaterGoal) : Int?{
        return transaction {
            WaterGoals.update ({
                WaterGoals.id eq waterGoalId}){
                it[userId] = waterGoalToUpdate.userId
                it[waterTarget] = waterGoalToUpdate.waterTarget
                it[entryTime] = DateTime.now()
            }
        }
    }
}