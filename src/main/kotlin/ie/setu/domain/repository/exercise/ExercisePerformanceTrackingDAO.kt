package ie.setu.domain.repository.exercise

import ie.setu.domain.db.exercise.ExercisePerformanceTrackings
import ie.setu.domain.exercise.ExercisePerformanceTracking
import ie.setu.domain.db.exercise.ExerciseLogs
import ie.setu.utils.mapToExercisePerformanceTracking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class ExercisePerformanceTrackingDAO {

    fun calculateUserPerformance(userId: Int): ExercisePerformanceTrackings? {
        val performanceTracking: ExercisePerformanceTrackings? = null

        transaction {
            val totalExercises = ExerciseLogs
                .selectAll().where { ExerciseLogs.userId eq userId }
                .count()
            val completedCount = ExerciseLogs
                .selectAll().where { (ExerciseLogs.userId eq userId) and (ExerciseLogs.status eq "completed") }
                .count()

            val missedCount = totalExercises - completedCount
            val performanceTrackingId = ExercisePerformanceTrackings.insert {
                it[performanceStatus] = if (missedCount == 0L) "Achieved" else "Missed"
                it[achievedCount] = completedCount.toInt()
                it[missCount] = missedCount.toInt()
                it[checkedAt] = DateTime.now()
                it[userPerId] = userId
            } get ExercisePerformanceTrackings.id

        }

       return performanceTracking
    }

    fun findByUserPerId(userPerId: Int): List<ExercisePerformanceTracking>{
        return transaction {
            ExercisePerformanceTrackings
                .selectAll().where {ExercisePerformanceTrackings.userPerId eq userPerId}
                .map { mapToExercisePerformanceTracking(it) }
        }
    }

    fun deleteByUserPerId (userPerId: Int): Int{
        return transaction{
            ExercisePerformanceTrackings.deleteWhere { ExercisePerformanceTrackings.userPerId eq userPerId }
        }
    }


    }

