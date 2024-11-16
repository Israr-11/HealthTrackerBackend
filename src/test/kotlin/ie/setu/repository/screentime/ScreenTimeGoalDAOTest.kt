package ie.setu.repository.screentime

import ie.setu.domain.screentime.ScreenTimeGoal
import ie.setu.domain.db.screentime.ScreenTimeGoals
import ie.setu.domain.repository.screentime.ScreenTimeGoalDAO
import ie.setu.helpers.populateScreenTimeGoalTable
import ie.setu.helpers.populateUserTable
import ie.setu.helpers.screenTimeGoals
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

val goalScreenTime1 = screenTimeGoals[0]
val goalScreenTime2 = screenTimeGoals[1]

class ScreenTimeGoalDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadScreenTimeGoals {
        @Test
        fun `get all goals from populated table`() {
            transaction {
                val userDAO = populateUserTable()
                val dao = populateScreenTimeGoalTable()
                assertEquals(2, dao.getAll().size)
            }
        }

        @Test
        fun `get all goals from empty table`() {
            transaction {
                SchemaUtils.create(ScreenTimeGoals)
                val userDAO = populateUserTable()
                val dao = ScreenTimeGoalDAO()
                assertEquals(0, dao.getAll().size)
            }
        }
    }

    @Nested
    inner class CreateScreenTimeGoals {
        @Test
        fun `add multiple goals and verify content`() {
            transaction {
                val userDAO = populateUserTable()
                val dao = populateScreenTimeGoalTable()
                val allgoals = dao.getAll()
                assertEquals(2, allgoals.size)
                assertEquals(goalScreenTime1.targetScreenHours  , allgoals[0].targetScreenHours  )
                assertEquals(goalScreenTime2.targetScreenHours  , allgoals[1].targetScreenHours  )
            }
        }
    }


    @Nested
    inner class UpdateGoals {
        @Test
        fun `updating existing gaols in table results in successful update`() {
            transaction {
                val userDAO = populateUserTable()
                val screenTimeGoalDAO = populateScreenTimeGoalTable()

                val screenTimeGoalupdated = ScreenTimeGoal(id = 1, userId = 3, targetScreenHours   = 7, entryTime = DateTime.now())
                screenTimeGoalDAO.updateByScreenTimeGoalId(screenTimeGoalupdated.id, screenTimeGoalupdated)
                assertEquals(screenTimeGoalupdated.userId, 3)
            }
        }
    }
}
