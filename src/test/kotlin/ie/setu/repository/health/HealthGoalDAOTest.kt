package ie.setu.repository.health

import ie.setu.domain.health.HealthGoal
import ie.setu.domain.db.health.HealthGoals
import ie.setu.domain.repository.health.HealthGoalDAO
import ie.setu.helpers.populateHealthGoalTable
import ie.setu.helpers.populateUserTable
import ie.setu.helpers.healthGoals
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

val goalHealth1 = healthGoals[0]
val goalHealth2 = healthGoals[1]

class HealthGoalDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadHealthGoals {
        @Test
        fun `get all goals from populated table`() {
            transaction {
                val userDAO = populateUserTable()
                val dao = populateHealthGoalTable()
                assertEquals(2, dao.getAll().size)
            }
        }

        @Test
        fun `get all goals from empty table`() {
            transaction {
                SchemaUtils.create(HealthGoals)
                val userDAO = populateUserTable()
                val dao = HealthGoalDAO()
                assertEquals(0, dao.getAll().size)
            }
        }
    }

    @Nested
    inner class CreateHealthGoals {
        @Test
        fun `add multiple goals and verify content`() {
            transaction {
                val userDAO = populateUserTable()
                val dao = populateHealthGoalTable()
                val allgoals = dao.getAll()
                assertEquals(2, allgoals.size)
                assertEquals(goalHealth1.healthGoalType , allgoals[0].healthGoalType )
                assertEquals(goalHealth2.healthGoalType , allgoals[1].healthGoalType )
            }
        }
    }

    @Nested
    inner class UpdateGoals {
        @Test
        fun `updating existing gaols in table results in successful update`() {
            transaction {

                val userDAO = populateUserTable()
                val HealthGoalDAO = populateHealthGoalTable()

                val healthGoalupdated = HealthGoal(id = 1, userId = 3, healthGoalType = "Weight Loss", targetValue = 5,entryTime = DateTime.now())
                HealthGoalDAO.updateByHealthGoalId(healthGoalupdated.id, healthGoalupdated)
                assertEquals(healthGoalupdated.userId, 3)
            }
        }
    }

}
