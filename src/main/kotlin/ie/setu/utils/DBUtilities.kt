package ie.setu.utils

import ie.setu.domain.Activity
import ie.setu.domain.User
import ie.setu.domain.db.Activities
import ie.setu.domain.db.exercise.ExercisePerformanceTrackings
import ie.setu.domain.db.Users
import ie.setu.domain.exercise.ExerciseSchedule
import ie.setu.domain.exercise.ExerciseLog
import ie.setu.domain.db.exercise.ExerciseLogs
import ie.setu.domain.db.exercise.ExerciseSchedules
import ie.setu.domain.exercise.ExercisePerformanceTracking
import kotlinx.serialization.json.Json

import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime

fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email]
)

fun mapToActivity(it: ResultRow) = Activity(
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userId]
)

fun mapToExerciseSchedule(it: ResultRow) = ExerciseSchedule(
    id = it[ExerciseSchedules.id],
    userId = it[ExerciseSchedules.userId],
    exerciseType=it[ExerciseSchedules.exerciseType],
    duration = it[ExerciseSchedules.duration],
    entryTime = it[ExerciseSchedules.entryTime]
)

fun mapToExerciseLog(it: ResultRow) = ExerciseLog(
    id = it[ExerciseLogs.id],
    userId = it[ExerciseLogs.userId],
    exerciseId=it[ExerciseLogs.exerciseId],
    status=it[ExerciseLogs.status],
    entryTime = it[ExerciseLogs.entryTime]
)



fun mapToExercisePerformanceTracking(it: ResultRow) = ExercisePerformanceTracking(
     id=it[ExercisePerformanceTrackings.id],
    userPerId = it[ExercisePerformanceTrackings.userPerId],
    performanceStatus=it[ExercisePerformanceTrackings.performanceStatus],
    achievedCount=it[ExercisePerformanceTrackings.achievedCount],
    missCount=it[ExercisePerformanceTrackings.missCount],
    checkedAt = it[ExercisePerformanceTrackings.checkedAt],
)

