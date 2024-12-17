package ie.setu.repository.mentalhealth

import ie.setu.domain.repository.mentalhealth.MentalHealthLogAndPerformanceDAO
import ie.setu.helpers.*

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

val logMentalHealth1 = mentalHealthLogsAndPerformances[0]
val logMentalHealth2 = mentalHealthLogsAndPerformances[1]

class MentalHealthLogAndPerformanceDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadMentalHealthLogs {
        @Test
        fun `get all logs by User Id from populated table`() {
            transaction {
                populateUserTable()
                populateMentalHealthGoalTable()
                populateMentalHealthLogAndPerformanceTable()
                val dao = MentalHealthLogAndPerformanceDAO()
                val objectSize = dao.findByUserId(3).size
                assertEquals(2, objectSize)
            }
        }
    }

    @Nested
    inner class CreateMentalHealthLogs {
        @Test
        fun `add multiple logs and verify content`() {
            transaction {
                populateUserTable()
                populateMentalHealthGoalTable()
                val dao = populateMentalHealthLogAndPerformanceTable()
                val allLogs = dao.findByUserId(3)
                assertEquals(2, allLogs.size)
                assertEquals(logMentalHealth1.userId, allLogs[0].userId)
                assertEquals(logMentalHealth2.userId, allLogs[1].userId)
            }
        }
    }

    @Nested
    inner class DeleteMentalHealthLogs {

        @Test
        fun `delete existing mental health log reduces table size`() {
            transaction {
                populateUserTable()
                populateMentalHealthGoalTable()
                val dao = populateMentalHealthLogAndPerformanceTable()
                assertEquals(2, dao.findByUserId(3).size)

                dao.deleteByMentalHealthGoalId(1)

                assertEquals(1, dao.findByUserId(3).size)
            }
        }
    }
}
