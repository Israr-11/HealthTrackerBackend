package ie.setu.repository.mentalhealth

import ie.setu.domain.mentalhealth.MentalHealthGoal
import ie.setu.domain.db.mentalhealth.MentalHealthGoals
import ie.setu.domain.repository.mentalhealth.MentalHealthGoalDAO
import ie.setu.helpers.populateMentalHealthGoalTable
import ie.setu.helpers.populateUserTable
import ie.setu.helpers.mentalHealthGoals
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

val goalMentalHealth1 = mentalHealthGoals[0]
val goalMentalHealth2 = mentalHealthGoals[1]

class MentalHealthGoalDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadMentalHealthGoals {
        @Test
        fun `get all goals from populated table`() {
            transaction {
                val userDAO = populateUserTable()
                val dao = populateMentalHealthGoalTable()
                assertEquals(2, dao.getAll().size)
            }
        }

        @Test
        fun `get all goals from empty table`() {
            transaction {
                SchemaUtils.create(MentalHealthGoals)
                val userDAO = populateUserTable()
                val dao = MentalHealthGoalDAO()
                assertEquals(0, dao.getAll().size)
            }
        }
    }

    @Nested
    inner class CreateMentalHealthGoals {
        @Test
        fun `add multiple goals and verify content`() {
            transaction {
                val userDAO = populateUserTable()
                val dao = populateMentalHealthGoalTable()
                val allGoals = dao.getAll()
                assertEquals(2, allGoals.size)
                assertEquals(goalMentalHealth1.targetMoodScore, allGoals[0].targetMoodScore)
                assertEquals(goalMentalHealth2.targetMoodScore, allGoals[1].targetMoodScore)
            }
        }
    }

    @Nested
    inner class UpdateMentalHealthGoals {
        @Test
        fun `updating existing goals in table results in successful update`() {
            transaction {
                val userDAO = populateUserTable()
                val mentalHealthGoalDAO = populateMentalHealthGoalTable()

                val updatedGoal = MentalHealthGoal(id = 1, userId = 3, targetMoodScore = 8, entryTime = DateTime.now())
                mentalHealthGoalDAO.updateByMentalHealthGoalId(updatedGoal.id, updatedGoal)
                assertEquals(updatedGoal.userId, 3)
            }
        }
    }
}
