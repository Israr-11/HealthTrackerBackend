package ie.setu.domain.repository.screentime

import ie.setu.domain.db.screentime.ScreenTimeGoals
import ie.setu.domain.screentime.ScreenTimeGoal
import ie.setu.utils.mapToScreenTimeGoal
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime


class ScreenTimeGoalDAO {


    fun getAll():  ArrayList<ScreenTimeGoal> {
        val screenTimeGoalList: ArrayList<ScreenTimeGoal> = arrayListOf()

        transaction {
            ScreenTimeGoals.selectAll().map {
                screenTimeGoalList.add(mapToScreenTimeGoal(it))
            }
        }
        return screenTimeGoalList
    }




    fun save(screen_time_goal: ScreenTimeGoal) : Int?{
        return transaction {
            val screenTimelogId=ScreenTimeGoals.insert {
                it[userId] = screen_time_goal.userId
                it[targetScreenHours ] = screen_time_goal.targetScreenHours
                it[entryTime] = DateTime.now()
            } get ScreenTimeGoals.id
            screenTimelogId
        }
    }

    fun updateByScreenTimeGoalId(screenTimeGoalId: Int,screenTimeGoalToUpdate: ScreenTimeGoal) : Int?{
        return transaction {
            ScreenTimeGoals.update ({
                ScreenTimeGoals.id eq screenTimeGoalId}){
                it[userId] = screenTimeGoalToUpdate.userId
                it[targetScreenHours] = screenTimeGoalToUpdate.targetScreenHours
                it[entryTime] = DateTime.now()
            }
        }
    }




}