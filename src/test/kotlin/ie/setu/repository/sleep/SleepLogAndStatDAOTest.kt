package ie.setu.repository.sleep

import ie.setu.domain.repository.sleep.SleepGoalLogAndStatsDAO
import ie.setu.helpers.*

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

val logSleep1 = sleepGoalLogsAndStats[0]
val logSleep2 = sleepGoalLogsAndStats[1]

class SleepLogAndStatDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadSleepLogs {
        @Test
        fun `get all logs by User Id from populated table`() {
            transaction {
                populateUserTable()
                populateSleepGoalTable()
                populateSleepGoalLogAndTable()
                val dao=SleepGoalLogAndStatsDAO()
                val objectSize=dao.findByUserId(3).size
                assertEquals(2, objectSize)
            }
        }
    }

    @Nested
    inner class CreateSleepLogs{
        @Test
        fun `add multiple logs and verify content`() {
            transaction {
                populateUserTable()
                populateSleepGoalTable()
                val dao=populateSleepGoalLogAndTable()
                val alllogs = dao.findByUserId(3)
                assertEquals(2, alllogs.size)
                assertEquals(logSleep1.actualSleepHours, alllogs[0].actualSleepHours)
                assertEquals(logSleep2.actualSleepHours, alllogs[1].actualSleepHours)
            }
        }
    }

    @Nested
    inner class DeleteSleepLogs {

        @Test
        fun `delete existing sleep log reduces table size`() {
            transaction {
                populateUserTable()
                populateSleepGoalTable()
                val dao=populateSleepGoalLogAndTable()
                assertEquals(2, dao.findByUserId(3).size)

                dao.deleteBySleepGoalId(1)

                assertEquals(1, dao.findByUserId(3).size)
            }
        }
    }


}
