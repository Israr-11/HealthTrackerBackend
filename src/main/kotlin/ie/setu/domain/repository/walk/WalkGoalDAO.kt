package ie.setu.domain.repository.walk

import ie.setu.domain.db.walk.WalkGoals
import ie.setu.domain.walk.WalkGoal
import ie.setu.utils.mapToWalkGoal
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime

class WalkGoalDAO {

    fun getAll(): ArrayList<WalkGoal> {
        val walkGoalsList: ArrayList<WalkGoal> = arrayListOf()

        transaction {
            WalkGoals.selectAll().map {
                walkGoalsList.add(mapToWalkGoal(it))
            }
        }
        return walkGoalsList
    }

    fun save(walkGoal: WalkGoal): Int? {
        return transaction {
            val walkGoalId = WalkGoals.insert {
                it[userId] = walkGoal.userId
                it[targetSteps] = walkGoal.targetSteps
                it[uphill] = walkGoal.uphill
                it[entryTime] = DateTime.now()
            } get WalkGoals.id
            walkGoalId
        }
    }

    fun updateByWalkGoalId(walkGoalId: Int, walkGoalToUpdate: WalkGoal): Int? {
        return transaction {
            WalkGoals.update({ WalkGoals.id eq walkGoalId }) {
                it[userId] = walkGoalToUpdate.userId
                it[targetSteps] = walkGoalToUpdate.targetSteps
                it[uphill] = walkGoalToUpdate.uphill
                it[entryTime] = DateTime.now()
            }
        }
    }
}
