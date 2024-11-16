package ie.setu.repository.health


import ie.setu.domain.repository.health.HealthGoalLogAndPerformanceDAO
import ie.setu.helpers.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

val logHealth1 = healthGoalLogsAndPerformances[0]
val logHealth2 = healthGoalLogsAndPerformances[1]

class HealthGoalLogAndPerformanceDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadHealthLogs {
        @Test
        fun `get all logs by User Id from populated table`() {
            transaction {
                populateUserTable()
                populateHealthGoalTable()
                populateHealthGoalLogAndPerformanceTable()
                val dao=HealthGoalLogAndPerformanceDAO()
                val objectSize=dao.findByUserId(3).size
                assertEquals(2, objectSize)
            }
        }
    }

    @Nested
    inner class CreateHealthLogs{
        @Test
        fun `add multiple logs and verify content`() {
            transaction {
                populateUserTable()
                populateHealthGoalTable()
                val dao= populateHealthGoalLogAndPerformanceTable()
                val alllogs = dao.findByUserId(3)
                assertEquals(2, alllogs.size)
                assertEquals(logHealth1.targetReached  , alllogs[0].targetReached  )
                assertEquals(logHealth2.targetReached  , alllogs[1].targetReached  )
            }
        }
    }

    @Nested
    inner class DeleteHealthLogs {

        @Test
        fun `delete existing health log reduces table size`() {
            transaction {
                populateUserTable()
                populateHealthGoalTable()
                val dao= populateHealthGoalLogAndPerformanceTable()
                assertEquals(2, dao.findByUserId(3).size)

                dao.deleteByHealthGoalId(1)

                assertEquals(1, dao.findByUserId(3).size)
            }
        }
    }


}
