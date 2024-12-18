package ie.setu.repository.walk

import ie.setu.domain.walk.WalkGoal
import ie.setu.domain.db.walk.WalkGoals
import ie.setu.domain.repository.walk.WalkGoalDAO
import ie.setu.helpers.populateUserTable
import ie.setu.helpers.populateWalkGoalTable
import ie.setu.helpers.walkGoals
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

val goalWalk1 = walkGoals[0]
val goalWalk2 = walkGoals[1]

class WalkGoalDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadWalkGoals {
        @Test
        fun `get all goals from populated table`() {
            transaction {
                populateUserTable()
                val dao = populateWalkGoalTable()
                assertEquals(2, dao.getAll().size)
            }
        }

        @Test
        fun `get all goals from empty table`() {
            transaction {
                SchemaUtils.create(WalkGoals)
                populateUserTable()
                val dao = WalkGoalDAO()
                assertEquals(0, dao.getAll().size)
            }
        }
    }

    @Nested
    inner class CreateWalkGoals {
        @Test
        fun `add multiple goals and verify content`() {
            transaction {
                populateUserTable()
                val dao = populateWalkGoalTable()
                val allGoals = dao.getAll()
                assertEquals(2, allGoals.size)
                assertEquals(goalWalk1.targetSteps, allGoals[0].targetSteps)
                assertEquals(goalWalk2.targetSteps, allGoals[1].targetSteps)
            }
        }
    }

    @Nested
    inner class UpdateWalkGoals {
        @Test
        fun `updating existing goals in table results in successful update`() {
            transaction {
                populateUserTable()
                val walkGoalDAO = populateWalkGoalTable()
                val updatedWalkGoal = WalkGoal(id = 1, userId = 3, targetSteps = 10000, uphill = true, entryTime = DateTime.now())
                walkGoalDAO.updateByWalkGoalId(walkGoalId = updatedWalkGoal.id, updatedWalkGoal)
                assertEquals(updatedWalkGoal.targetSteps, 10000)
            }
        }
    }
}
