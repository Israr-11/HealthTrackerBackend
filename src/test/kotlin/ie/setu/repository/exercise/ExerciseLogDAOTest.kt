package ie.setu.repository.exercise

import ie.setu.domain.db.exercise.ExerciseLogs
import ie.setu.domain.repository.exercise.ExerciseLogDAO
import ie.setu.helpers.populateExerciseLogTable
import ie.setu.helpers.populateExerciseScheduleTable
import ie.setu.helpers.populateUserTable
import ie.setu.helpers.logsExercise
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

val log1 = logsExercise[0]
val log2 = logsExercise[1]

class ExerciseLogDAOTest {

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
        fun `get all logs from populated table`() {
            transaction {
                val userDAO = populateUserTable()
                val exerciseLogDAO = populateExerciseScheduleTable()
                val dao = populateExerciseLogTable()
                assertEquals(2, dao.getAll().size)
            }
        }

        @Test
        fun `get all logs from empty table`() {
            transaction {
                SchemaUtils.create(ExerciseLogs)
                val userDAO = populateUserTable()
                val exerciseLogDAO = populateExerciseScheduleTable()
                val dao = ExerciseLogDAO()
                assertEquals(0, dao.getAll().size)
            }
        }
    }

    @Nested
    inner class CreateExerciseSchedules {
        @Test
        fun `add multiple log and verify content`() {
            transaction {
                val userDAO = populateUserTable()
                val exerciseLogDAO = populateExerciseScheduleTable()
                val dao = populateExerciseLogTable()
                val allLogs = dao.getAll()
                assertEquals(2, allLogs.size)
                assertEquals(log1.status, allLogs[0].status)
                assertEquals(log2.status, allLogs[1].status)
            }
        }
    }

    @Nested
    inner class DeleteExerciseSchedules {
        @Test
        fun `delete non-existent log does not affect table`() {
            transaction {
                val userDAO = populateUserTable()
                val exerciseLogDAO = populateExerciseScheduleTable()
                val dao = populateExerciseLogTable()
                assertEquals(2, dao.getAll().size)

                dao.deleteByExerciseLogId(9)
                assertEquals(2, dao.getAll().size)
            }
        }

        @Test
        fun `delete existing logs reduces table size`() {
            transaction {
                val userDAO = populateUserTable()
                val exerciseLogDAO = populateExerciseScheduleTable()
                val dao = populateExerciseLogTable()
                assertEquals(2, dao.getAll().size)

                dao.deleteByExerciseLogId(schedule1.id)
                val remainingLogs = dao.getAll()
                assertEquals(1, remainingLogs.size)
                assertEquals(log2.status, remainingLogs[0].status)
            }
        }
    }
}
