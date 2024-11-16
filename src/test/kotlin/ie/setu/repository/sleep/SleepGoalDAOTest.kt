package ie.setu.repository.sleep

import ie.setu.domain.sleep.SleepGoal
import ie.setu.domain.db.sleep.SleepGoals
import ie.setu.domain.repository.sleep.SleepGoalDAO
import ie.setu.helpers.populateSleepGoalTable
import ie.setu.helpers.populateUserTable
import ie.setu.helpers.goalsSleep
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

val goal1 = goalsSleep[0]
val goal2 = goalsSleep[1]

class SleepGoalDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadSleepGoals {
        @Test
        fun `get all goals from populated table`() {
            transaction {
                val userDAO = populateUserTable()
                val dao = populateSleepGoalTable()
                assertEquals(2, dao.getAll().size)
            }
        }

        @Test
        fun `get all goals from empty table`() {
            transaction {
                SchemaUtils.create(SleepGoals)
                val userDAO = populateUserTable()
                val dao = SleepGoalDAO()
                assertEquals(0, dao.getAll().size)
            }
        }
    }

    @Nested
    inner class CreateSleepGoals {
        @Test
        fun `add multiple goals and verify content`() {
            transaction {
                val userDAO = populateUserTable()
                val dao = populateSleepGoalTable()
                val allgoals = dao.getAll()
                assertEquals(2, allgoals.size)
                assertEquals(goal1.targetSleepHours, allgoals[0].targetSleepHours)
                assertEquals(goal2.targetSleepHours, allgoals[1].targetSleepHours)
            }
        }
    }


    @Nested
    inner class UpdateGoals {
        @Test
        fun `updating existing gaols in table results in successful update`() {
            transaction {
                val userDAO = populateUserTable()
                val sleepGoalDAO = populateSleepGoalTable()

                val sleepGoalupdated = SleepGoal(id = 1, userId = 3, targetSleepHours = 8, targetSleepQuality = "Good", targetSleepTiming = "10:00 PM", entryTime = DateTime.now())
                sleepGoalDAO.updateBySleepGoalId(sleepGoalupdated.id, sleepGoalupdated)
                assertEquals(sleepGoalupdated.userId, 3)
            }
        }
    }
}
