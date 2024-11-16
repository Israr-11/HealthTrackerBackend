package ie.setu.repository.exercise

import ie.setu.domain.repository.exercise.ExercisePerformanceTrackingDAO
import ie.setu.helpers.populateExerciseLogTable
import ie.setu.helpers.populateExerciseScheduleTable
import ie.setu.helpers.populateExercisePerformanceTable
import ie.setu.helpers.populateUserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ExercisePerformanceTrackingDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    val userPerId = 3
    @Nested
    inner class GetPerformanceTracking {


        @Test
        fun `get performance tracking by userPerId`() {
            transaction {
                populateUserTable()
                populateExerciseScheduleTable()
                populateExerciseLogTable()
                populateExercisePerformanceTable()
                val dao = ExercisePerformanceTrackingDAO()
                val performance = dao.findByUserPerId(userPerId)
                assertTrue(performance.isNotEmpty(), "Performance data should not be empty")
                assertEquals(userPerId, performance[0].userPerId, "The userPerId should match")
            }
        }
    }

    @Nested
    inner class DeletePerformanceTracking {
        @Test
        fun `delete performance tracking by userPerId`() {
            transaction {
                populateUserTable()
                populateExerciseScheduleTable()
                populateExerciseLogTable()
                populateExercisePerformanceTable()

                val dao = ExercisePerformanceTrackingDAO()

                val performanceBeforeDelete = dao.findByUserPerId(userPerId)
                assertNotNull(performanceBeforeDelete, "Performance before deletion should exist")

                val rowsDeletedFirst = dao.deleteByUserPerId(userPerId)
                assertEquals(1, rowsDeletedFirst, "One record should be deleted")

                val rowsDeletedSecond = dao.deleteByUserPerId(userPerId)
                assertEquals(0, rowsDeletedSecond, "No records should be deleted after the first deletion")
            }
        }

        @Test
        fun `delete performance tracking for non-existent userPerId does nothing`() {
            transaction {
                populateUserTable()
                populateExerciseScheduleTable()
                populateExerciseLogTable()
                populateExercisePerformanceTable()

                val dao = ExercisePerformanceTrackingDAO()

                val rowsDeleted = dao.deleteByUserPerId(999)
                assertEquals(0, rowsDeleted, "Deleting a non-existent userPerId should not affect any rows")
            }
        }
    }
}
