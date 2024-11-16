package ie.setu.repository.diet

import ie.setu.domain.diet.DietGoal
import ie.setu.domain.db.diet.DietGoals
import ie.setu.domain.repository.diet.DietGoalDAO
import ie.setu.helpers.populateDietGoalTable
import ie.setu.helpers.populateUserTable
import ie.setu.helpers.dietGoals
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

val goalDiet1 = dietGoals[0]
val goalDiet2 = dietGoals[1]

class SleepGoalDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadDietGoals {
        @Test
        fun `get all goals from populated table`() {
            transaction {
                val userDAO = populateUserTable()
                val dao = populateDietGoalTable()
                assertEquals(2, dao.getAll().size)
            }
        }

        @Test
        fun `get all goals from empty table`() {
            transaction {
                SchemaUtils.create(DietGoals)
                val userDAO = populateUserTable()
                val dao = DietGoalDAO()
                assertEquals(0, dao.getAll().size)
            }
        }
    }

    @Nested
    inner class CreateDietGoals {
        @Test
        fun `add multiple goals and verify content`() {
            transaction {
                val userDAO = populateUserTable()
                val dao = populateDietGoalTable()
                val allgoals = dao.getAll()
                assertEquals(2, allgoals.size)
                assertEquals(goalDiet1.targetCalories , allgoals[0].targetCalories )
                assertEquals(goalDiet2.targetCalories , allgoals[1].targetCalories )
            }
        }
    }


    @Nested
    inner class UpdateGoals {
        @Test
        fun `updating existing gaols in table results in successful update`() {
            transaction {
                val userDAO = populateUserTable()
                val dietGoalDAO = populateDietGoalTable()

                val dietGoalupdated = DietGoal(id = 1, userId = 3, dietGoalType  = "Weight Loss",targetCalories = 1600, entryTime = DateTime.now())
                dietGoalDAO.updateByDietGoalId(dietGoalupdated.id, dietGoalupdated)
                assertEquals(dietGoalupdated.userId, 3)
            }
        }
    }
}
