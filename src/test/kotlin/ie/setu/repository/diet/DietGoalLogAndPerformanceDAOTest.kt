package ie.setu.repository.diet


import ie.setu.domain.repository.diet.DietGoalLogAndPerformanceDAO
import ie.setu.helpers.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

val logDiet1 = dietGoalLogsAndPerformances[0]
val logDiet2 = dietGoalLogsAndPerformances[1]

class HealthGoalLogAndPerformanceDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadDietLogs {
        @Test
        fun `get all logs by User Id from populated table`() {
            transaction {
                populateUserTable()
                populateDietGoalTable()
                populateDietGoalLogAndPerformanceTable()
                val dao=DietGoalLogAndPerformanceDAO()
                val objectSize=dao.findByUserId(3).size
                assertEquals(2, objectSize)
            }
        }
    }

    @Nested
    inner class CreateDietLogs{
        @Test
        fun `add multiple logs and verify content`() {
            transaction {
                populateUserTable()
                populateDietGoalTable()
                val dao= populateDietGoalLogAndPerformanceTable()
                val alllogs = dao.findByUserId(3)
                assertEquals(2, alllogs.size)
                assertEquals(logDiet1.userId   , alllogs[0].userId   )
                assertEquals(logDiet2.userId   , alllogs[1].userId   )
            }
        }
    }

    @Nested
    inner class DeleteDietLogs {

        @Test
        fun `delete existing diet log reduces table size`() {
            transaction {
                populateUserTable()
                populateDietGoalTable()
                val dao= populateDietGoalLogAndPerformanceTable()
                assertEquals(2, dao.findByUserId(3).size)

                dao.deleteByDietGoalId(1)

                assertEquals(1, dao.findByUserId(3).size)
            }
        }
    }


}
