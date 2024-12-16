package ie.setu.repository.water


import ie.setu.domain.repository.water.WaterGoalLogAndStatsDAO
import ie.setu.helpers.*

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

val logWaterTime1 = waterLogAndStat[0]
val logWaterTime2 = waterLogAndStat[1]

class ScreenTimeGoalLogAndPerformanceDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadWaterLogs {
        @Test
        fun `get all logs by User Id from populated table`() {
            transaction {
                populateUserTable()
                populateWaterGoalTable()
              populateWaterLogAndPerformanceTable()
                val dao=WaterGoalLogAndStatsDAO()
                val objectSize=dao.findByUserId(3).size
                assertEquals(2, objectSize)
            }
        }
    }

    @Nested
    inner class CreateWaterLogs{
        @Test
        fun `add multiple logs and verify content`() {
            transaction {
                populateUserTable()
                populateWaterGoalTable()
                val dao= populateWaterLogAndPerformanceTable()
                val alllogs = dao.findByUserId(3)
                assertEquals(2, alllogs.size)
                assertEquals(logWaterTime1.userId   , alllogs[0].userId   )
                assertEquals(logWaterTime2.userId   , alllogs[1].userId   )
            }
        }
    }

    @Nested
    inner class DeleteWaterLogs {

        @Test
        fun `delete existing screen time log reduces table size`() {
            transaction {
                populateUserTable()
                populateWaterGoalTable()
                val dao= populateWaterLogAndPerformanceTable()
                assertEquals(2, dao.findByUserId(3).size)

                dao.deleteByWaterGoalId(1)

                assertEquals(1, dao.findByUserId(3).size)
            }
        }
    }


}
