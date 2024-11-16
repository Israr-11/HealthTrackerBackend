package ie.setu.repository.screentime


import ie.setu.domain.repository.screentime.ScreenTimeLogAndPerformanceDAO
import ie.setu.helpers.*

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

val logScreenTime1 = screenTimeLogsAndPerformances[0]
val logScreenTime2 = screenTimeLogsAndPerformances[1]

class ScreenTimeGoalLogAndPerformanceDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadScreenTimeLogs {
        @Test
        fun `get all logs by User Id from populated table`() {
            transaction {
                populateUserTable()
                populateScreenTimeGoalTable()
              populateScreenTimeLogAndPerformanceTable()
                val dao=ScreenTimeLogAndPerformanceDAO()
                val objectSize=dao.findByUserId(3).size
                assertEquals(2, objectSize)
            }
        }
    }

    @Nested
    inner class CreateScreenTimeLogs{
        @Test
        fun `add multiple logs and verify content`() {
            transaction {
                populateUserTable()
                populateScreenTimeGoalTable()
                val dao= populateScreenTimeLogAndPerformanceTable()
                val alllogs = dao.findByUserId(3)
                assertEquals(2, alllogs.size)
                assertEquals(logScreenTime1.userId   , alllogs[0].userId   )
                assertEquals(logScreenTime2.userId   , alllogs[1].userId   )
            }
        }
    }

    @Nested
    inner class DeleteScreenTimeLogs {

        @Test
        fun `delete existing screen time log reduces table size`() {
            transaction {
                populateUserTable()
                populateScreenTimeGoalTable()
                val dao= populateScreenTimeLogAndPerformanceTable()
                assertEquals(2, dao.findByUserId(3).size)

                dao.deleteByScreenTimeGoalId(1)

                assertEquals(1, dao.findByUserId(3).size)
            }
        }
    }


}
