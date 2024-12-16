package ie.setu.repository.water

import ie.setu.domain.water.WaterGoal
import ie.setu.domain.db.water.WaterGoals
import ie.setu.domain.repository.water.WaterGoalDAO
import ie.setu.helpers.populateWaterGoalTable
import ie.setu.helpers.populateUserTable
import ie.setu.helpers.waterGoals
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

val goalWater1 = waterGoals[0]
val goalWater2 = waterGoals[1]

class ScreenTimeGoalDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadWaterGoals {
        @Test
        fun `get all goals from populated table`() {
            transaction {
                val userDAO = populateUserTable()
                val dao = populateWaterGoalTable()
                assertEquals(2, dao.getAll().size)
            }
        }

        @Test
        fun `get all goals from empty table`() {
            transaction {
                SchemaUtils.create(WaterGoals)
                val userDAO = populateUserTable()
                val dao = WaterGoalDAO()
                assertEquals(0, dao.getAll().size)
            }
        }
    }

    @Nested
    inner class CreateWaterGoals {
        @Test
        fun `add multiple goals and verify content`() {
            transaction {
                val userDAO = populateUserTable()
                val dao = populateWaterGoalTable()
                val allgoals = dao.getAll()
                assertEquals(2, allgoals.size)
                assertEquals(goalWater1.waterTarget  , allgoals[0].waterTarget  )
                assertEquals(goalWater2.waterTarget  , allgoals[1].waterTarget  )
            }
        }
    }


    @Nested
    inner class UpdateGoals {
        @Test
        fun `updating existing gaols in table results in successful update`() {
            transaction {
                val userDAO = populateUserTable()
                val waterGoalDAO = populateWaterGoalTable()

                val screenTimeGoalupdated = WaterGoal(id = 1, userId = 3, waterTarget   = 7, entryTime = DateTime.now())
                waterGoalDAO.updateByWaterGoalId(waterGoalId = screenTimeGoalupdated.id, screenTimeGoalupdated)
                assertEquals(screenTimeGoalupdated.userId, 3)
            }
        }
    }
}
