package ie.setu.utils

import ie.setu.domain.User
import ie.setu.domain.db.exercise.ExercisePerformanceTrackings
import ie.setu.domain.db.Users
import ie.setu.domain.exercise.ExerciseSchedule
import ie.setu.domain.exercise.ExerciseLog
import ie.setu.domain.db.exercise.ExerciseLogs
import ie.setu.domain.db.exercise.ExerciseSchedules
import ie.setu.domain.exercise.ExercisePerformanceTracking
import ie.setu.domain.health.HealthGoal
import ie.setu.domain.health.HealthGoalLogAndPerformance
import ie.setu.domain.db.health.HealthGoals
import ie.setu.domain.db.health.HealthGoalLogAndPerformances
import ie.setu.domain.db.diet.DietGoals
import ie.setu.domain.db.diet.DietGoalLogAndPerformances
import ie.setu.domain.diet.DietGoal
import ie.setu.domain.diet.DietGoalLogAndPerformance
import ie.setu.domain.sleep.SleepGoal
import ie.setu.domain.sleep.SleepGoalLogAndStat
import ie.setu.domain.db.sleep.SleepGoals
import ie.setu.domain.db.sleep.SleepGoalLogAndStats
import ie.setu.domain.screentime.ScreenTimeGoal
import ie.setu.domain.screentime.ScreenTimeLogAndPerformance
import ie.setu.domain.db.screentime.ScreenTimeGoals
import ie.setu.domain.db.screentime.ScreenTimeLogAndPerformances
import ie.setu.domain.db.water.WaterGoalLogAndStats
import ie.setu.domain.db.water.WaterGoals
import ie.setu.domain.water.WaterGoal
import ie.setu.domain.water.WaterLogAndStat
import ie.setu.domain.db.mentalhealth.MentalHealthGoals
import ie.setu.domain.db.mentalhealth.MentalHealthLogAndPerformances
import ie.setu.domain.db.walk.WalkGoalLogAndStats
import ie.setu.domain.db.walk.WalkGoals
import ie.setu.domain.mentalhealth.MentalHealthGoal
import ie.setu.domain.mentalhealth.MentalHealthLogAndPerformance
import ie.setu.domain.walk.WalkGoal
import ie.setu.domain.walk.WalkLogAndStat
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime

fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email]
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

fun mapToHealthGoal(it: ResultRow) = HealthGoal(
    id=it[HealthGoals.id],
    userId = it[HealthGoals.userId],
    healthGoalType=it[HealthGoals.healthGoalType],
    targetValue =it[HealthGoals.targetValue],
    entryTime = it[HealthGoals.entryTime],
)

fun mapToHealthGoalLogAndPerformance(it: ResultRow) = HealthGoalLogAndPerformance(
    id=it[HealthGoalLogAndPerformances.id],
    userId = it[HealthGoalLogAndPerformances.userId],
    healthGoalId =it[HealthGoalLogAndPerformances.healthGoalId],
   achievedValue = it[HealthGoalLogAndPerformances.achievedValue],
    status = it[HealthGoalLogAndPerformances.status],
    targetReached = it[HealthGoalLogAndPerformances.targetReached],
    remarks = it[HealthGoalLogAndPerformances.remarks],
    entryTime = it[HealthGoalLogAndPerformances.entryTime],
)

fun mapToDietGoal(it: ResultRow) = DietGoal(
    id = it[DietGoals.id],
    userId = it[DietGoals.userId],
    dietGoalType = it[DietGoals.dietGoalType],
    targetCalories = it[DietGoals.targetCalories],
    entryTime = it[DietGoals.entryTime]
)

fun mapToDietGoalLogAndPerformance(it: ResultRow) = DietGoalLogAndPerformance(
    id = it[DietGoalLogAndPerformances.id],
    userId = it[DietGoalLogAndPerformances.userId],
    dietGoalId = it[DietGoalLogAndPerformances.dietGoalId],
    entryTime = it[DietGoalLogAndPerformances.entryTime],
    caloriesConsumed = it[DietGoalLogAndPerformances.caloriesConsumed],
    recommendations = it[DietGoalLogAndPerformances.recommendations],
    status = it[DietGoalLogAndPerformances.status],
    targetReached = it[DietGoalLogAndPerformances.targetReached],
    deficitSurplus = it[DietGoalLogAndPerformances.deficitSurplus]
)

fun mapToSleepGoal(row: ResultRow) = SleepGoal(
    id = row[SleepGoals.id],
    userId = row[SleepGoals.userId],
    targetSleepHours = row[SleepGoals.targetSleepHours],
    targetSleepQuality = row[SleepGoals.targetSleepQuality],
    targetSleepTiming = row[SleepGoals.targetSleepTiming],
    entryTime = row[SleepGoals.entryTime]
)

fun mapToSleepGoalLogAndStat(row: ResultRow) = SleepGoalLogAndStat(
    id = row[SleepGoalLogAndStats.id],
    userId = row[SleepGoalLogAndStats.userId],
    sleepGoalId = row[SleepGoalLogAndStats.sleepGoalId],
    entryTime = row[SleepGoalLogAndStats.entryTime],
    actualSleepHours = row[SleepGoalLogAndStats.actualSleepHours],
    actualSleepQuality = row[SleepGoalLogAndStats.actualSleepQuality],
    actualSleepTiming = row[SleepGoalLogAndStats.actualSleepTiming ],
    sleepInterruptions = row[SleepGoalLogAndStats.sleepInterruptions],
    sleepPhaseDetails = row[SleepGoalLogAndStats.sleepPhaseDetails],
    sleepQualityIndex = row[SleepGoalLogAndStats.sleepQualityIndex],
    remarks = row[SleepGoalLogAndStats.remarks]
)

fun mapToScreenTimeGoal(row: ResultRow) = ScreenTimeGoal(
    id = row[ScreenTimeGoals.id],
    userId = row[ScreenTimeGoals.userId],
    targetScreenHours  = row[ScreenTimeGoals.targetScreenHours ],
    entryTime = row[ScreenTimeGoals.entryTime]
)

fun mapToScreenTimeLogAndPerformance(row: ResultRow) = ScreenTimeLogAndPerformance (
        id = row[ScreenTimeLogAndPerformances.id],
        userId = row[ScreenTimeLogAndPerformances.userId],
        screenTimeGoalId = row[ScreenTimeLogAndPerformances.screenTimeGoalId],
        actualScreenHours = row[ScreenTimeLogAndPerformances.actualScreenHours],
        targetMet = row[ScreenTimeLogAndPerformances.targetMet],
        extraHours = row[ScreenTimeLogAndPerformances.extraHours],
        recommendations = row[ScreenTimeLogAndPerformances.recommendations],
        entryTime = row[ScreenTimeLogAndPerformances.entryTime]
)

fun mapToWaterGoal(row: ResultRow) = WaterGoal(
    id = row[WaterGoals.id],
    userId = row[WaterGoals.userId],
    waterTarget  = row[WaterGoals.waterTarget ],
    entryTime = row[WaterGoals.entryTime]
)

fun mapToWaterLogAndStats(row: ResultRow) = WaterLogAndStat (
    id = row[WaterGoalLogAndStats.id],
    userId = row[WaterGoalLogAndStats.userId],
    waterGoalId = row[WaterGoalLogAndStats.waterGoalId],
    actualWaterIntake = row[WaterGoalLogAndStats.actualWaterIntake],
    targetMet = row[WaterGoalLogAndStats.targetMet],
    shortfall = row[WaterGoalLogAndStats.shortfall],
    recommendations = row[WaterGoalLogAndStats.recommendations],
    entryTime = row[WaterGoalLogAndStats.entryTime]
)

fun mapToMentalHealthGoal(row: ResultRow) = MentalHealthGoal(
    id = row[MentalHealthGoals.id],
    userId = row[MentalHealthGoals.userId],
    targetMoodScore = row[MentalHealthGoals.targetMoodScore],
    entryTime = row[MentalHealthGoals.entryTime]
)

fun mapToMentalHealthLogAndPerformance(row: ResultRow) = MentalHealthLogAndPerformance(
    id = row[MentalHealthLogAndPerformances.id],
    userId = row[MentalHealthLogAndPerformances.userId],
    mentalHealthGoalId = row[MentalHealthLogAndPerformances.mentalHealthGoalId],
    moodScore = row[MentalHealthLogAndPerformances.moodScore],
    stressLevel = row[MentalHealthLogAndPerformances.stressLevel],
    targetMet = row[MentalHealthLogAndPerformances.targetMet],
    recommendations = row[MentalHealthLogAndPerformances.recommendations],
    entryTime = row[MentalHealthLogAndPerformances.entryTime]
)

fun mapToWalkGoal(row: ResultRow) = WalkGoal(
    id = row[WalkGoals.id],
    userId = row[WalkGoals.userId],
    targetSteps = row[WalkGoals.targetSteps],
    uphill = row[WalkGoals.uphill],
    entryTime = row[WalkGoals.entryTime]
)

fun mapToWalkLogAndStat(row: ResultRow) = WalkLogAndStat(
    id = row[WalkGoalLogAndStats.id],
    userId = row[WalkGoalLogAndStats.userId],
    walkGoalId = row[WalkGoalLogAndStats.walkGoalId],
    actualSteps = row[WalkGoalLogAndStats.actualSteps],
    targetMet = row[WalkGoalLogAndStats.targetMet],
    extraSteps = row[WalkGoalLogAndStats.extraSteps],
    walkQuality = row[WalkGoalLogAndStats.walkQuality],
    recommendations = row[WalkGoalLogAndStats.recommendations],
    entryTime = row[WalkGoalLogAndStats.entryTime]
)
