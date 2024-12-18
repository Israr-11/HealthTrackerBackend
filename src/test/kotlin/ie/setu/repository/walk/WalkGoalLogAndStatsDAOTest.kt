package ie.setu.repository.walk

import ie.setu.domain.repository.walk.WalkGoalLogAndStatsDAO
import ie.setu.helpers.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

val logWalkTime1 = walkLogsAndStats[0]
val logWalkTime2 = walkLogsAndStats[1]

class WalkGoalLogAndStatsDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadWalkLogs {
        @Test
        fun `get all logs by User Id from populated table`() {
            transaction {
                populateUserTable()
                populateWalkGoalTable()
                populateWalkGoalLogAndStatTable()
                val dao = WalkGoalLogAndStatsDAO()
                val objectSize = dao.findByUserId(3).size
                assertEquals(2, objectSize)
            }
        }
    }

    @Nested
    inner class CreateWalkLogs {
        @Test
        fun `add multiple logs and verify content`() {
            transaction {
                populateUserTable()
                populateWalkGoalTable()
                val dao = populateWalkGoalLogAndStatTable()
                val allLogs = dao.findByUserId(3)
                assertEquals(2, allLogs.size)
                assertEquals(logWalkTime1.userId, allLogs[0].userId)
                assertEquals(logWalkTime2.userId, allLogs[1].userId)
            }
        }
    }

    @Nested
    inner class DeleteWalkLogs {

        @Test
        fun `delete existing walk time log reduces table size`() {
            transaction {
                populateUserTable()
                populateWalkGoalTable()
                val dao = populateWalkGoalLogAndStatTable()
                assertEquals(2, dao.findByUserId(3).size)

                dao.deleteByWalkGoalId(1)

                assertEquals(1, dao.findByUserId(3).size)
            }
        }
    }
}
