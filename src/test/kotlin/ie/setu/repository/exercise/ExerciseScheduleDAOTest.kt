package ie.setu.repository.exercise

import ie.setu.domain.exercise.ExerciseSchedule
import ie.setu.domain.db.exercise.ExerciseSchedules
import ie.setu.domain.repository.exercise.ExerciseScheduleDAO
import ie.setu.helpers.populateExerciseScheduleTable
import ie.setu.helpers.populateUserTable
import ie.setu.helpers.schedulesExercise
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

val schedule1 = schedulesExercise[0]
val schedule2 = schedulesExercise[1]

class ExerciseScheduleDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadExerciseSchedules {
        @Test
        fun `get all schedules from populated table`() {
            transaction {
                val userDAO = populateUserTable()
                val dao = populateExerciseScheduleTable()
                assertEquals(2, dao.getAll().size)
            }
        }

        @Test
        fun `get all schedules from empty table`() {
            transaction {
                SchemaUtils.create(ExerciseSchedules)
                val userDAO = populateUserTable()
                val dao = ExerciseScheduleDAO()
                assertEquals(0, dao.getAll().size)
            }
        }
    }

    @Nested
    inner class CreateExerciseSchedules {
        @Test
        fun `add multiple schedules and verify content`() {
            transaction {
                val userDAO = populateUserTable()
                val dao = populateExerciseScheduleTable()
                val allSchedules = dao.getAll()
                assertEquals(2, allSchedules.size)
                assertEquals(schedule1.exerciseType, allSchedules[0].exerciseType)
                assertEquals(schedule2.exerciseType, allSchedules[1].exerciseType)
            }
        }
    }

    @Nested
    inner class DeleteExerciseSchedules {
        @Test
        fun `delete non-existent schedule does not affect table`() {
            transaction {
                val userDAO = populateUserTable()
                val dao = populateExerciseScheduleTable()
                assertEquals(2, dao.getAll().size)

                dao.deleteByExerciseScheduleId(9)
                assertEquals(2, dao.getAll().size)
            }
        }

        @Test
        fun `delete existing schedule reduces table size`() {
            transaction {
                val userDAO = populateUserTable()
                val dao = populateExerciseScheduleTable()
                assertEquals(2, dao.getAll().size)

                dao.deleteByExerciseScheduleId(schedule1.id)
                val remainingSchedules = dao.getAll()
                assertEquals(1, remainingSchedules.size)
                assertEquals(schedule2.exerciseType, remainingSchedules[0].exerciseType)
            }
        }
    }
}
