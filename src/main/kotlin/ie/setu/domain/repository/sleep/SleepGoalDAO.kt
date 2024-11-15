
package ie.setu.domain.repository.sleep

import ie.setu.domain.sleep.SleepGoal
import ie.setu.domain.db.sleep.SleepGoals
import ie.setu.utils.mapToSleepGoal
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime


class SleepGoalDAO {


    fun getAll():  ArrayList<SleepGoal> {
        val sleepGoalList: ArrayList<SleepGoal> = arrayListOf()

        transaction {
            SleepGoals.selectAll().map {
                sleepGoalList.add(mapToSleepGoal(it))
            }
        }
        return sleepGoalList
    }


    fun save(sleep_goal: SleepGoal) : Int?{
        return transaction {
            val sleeplogId=SleepGoals.insert {
                it[userId] = sleep_goal.userId
                it[targetSleepHours] = sleep_goal.targetSleepHours
                it[targetSleepQuality] = sleep_goal.targetSleepQuality
                it[targetSleepTiming] = sleep_goal.targetSleepTiming
                it[entryTime] = DateTime.now()
            } get SleepGoals.id
            sleeplogId
        }
    }

    fun updateBySleepGoalId(sleepGoalId: Int,sleepGoalToUpdate: SleepGoal) : Int?{
        return transaction {
            SleepGoals.update ({
                SleepGoals.id eq sleepGoalId}){
                it[userId] = sleepGoalToUpdate.userId
                it[targetSleepHours] = sleepGoalToUpdate.targetSleepHours
                it[targetSleepQuality] = sleepGoalToUpdate.targetSleepQuality
                it[targetSleepTiming] = sleepGoalToUpdate.targetSleepTiming
                it[entryTime] = DateTime.now()
            }
        }
    }
}