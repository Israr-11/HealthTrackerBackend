package ie.setu.helpers

import ie.setu.domain.User
import ie.setu.domain.exercise.ExerciseSchedule
import ie.setu.domain.db.exercise.ExerciseSchedules
import ie.setu.domain.exercise.ExerciseLog
import ie.setu.domain.db.exercise.ExerciseLogs
import ie.setu.domain.exercise.ExercisePerformanceTracking
import ie.setu.domain.db.exercise.ExercisePerformanceTrackings
import ie.setu.domain.sleep.SleepGoal
import ie.setu.domain.db.sleep.SleepGoals
import ie.setu.domain.sleep.SleepGoalLogAndStat
import ie.setu.domain.db.sleep.SleepGoalLogAndStats
import ie.setu.domain.health.HealthGoal
import ie.setu.domain.db.health.HealthGoals
import ie.setu.domain.health.HealthGoalLogAndPerformance
import ie.setu.domain.db.health.HealthGoalLogAndPerformances
import ie.setu.domain.diet.DietGoal
import ie.setu.domain.db.diet.DietGoals
import ie.setu.domain.diet.DietGoalLogAndPerformance
import ie.setu.domain.db.diet.DietGoalLogAndPerformances
import ie.setu.domain.screentime.ScreenTimeGoal
import ie.setu.domain.db.screentime.ScreenTimeGoals
import ie.setu.domain.screentime.ScreenTimeLogAndPerformance
import ie.setu.domain.db.screentime.ScreenTimeLogAndPerformances
import ie.setu.domain.db.Users
import ie.setu.domain.repository.UserDAO
import ie.setu.domain.repository.exercise.ExerciseScheduleDAO
import ie.setu.domain.repository.exercise.ExerciseLogDAO
import ie.setu.domain.repository.exercise.ExercisePerformanceTrackingDAO
import ie.setu.domain.repository.sleep.SleepGoalDAO
import ie.setu.domain.repository.sleep.SleepGoalLogAndStatsDAO
import ie.setu.domain.repository.health.HealthGoalDAO
import ie.setu.domain.repository.health.HealthGoalLogAndPerformanceDAO
import ie.setu.domain.repository.diet.DietGoalDAO
import ie.setu.domain.repository.diet.DietGoalLogAndPerformanceDAO
import ie.setu.domain.repository.screentime.ScreenTimeGoalDAO
import ie.setu.domain.repository.screentime.ScreenTimeLogAndPerformanceDAO
import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime


//Test Data
val userId=3

val users = arrayListOf<User>(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3),
)

val schedulesExercise = arrayListOf<ExerciseSchedule>(
    ExerciseSchedule(duration = 30, exerciseType = "Walking", userId = 3,id=1,entryTime= DateTime.now()),
    ExerciseSchedule(duration = 40, exerciseType = "Running", userId = 3,id=2,entryTime= DateTime.now()),
)

val logsExercise = arrayListOf<ExerciseLog>(
    ExerciseLog(exerciseId = 1, status = "completed", userId = 3,id=1,entryTime= DateTime.now()),
    ExerciseLog(exerciseId = 2, status = "not_completed", userId = 3,id=2,entryTime= DateTime.now()),
)

val goalsSleep=arrayListOf<SleepGoal>(
    SleepGoal(id = 1, userId = 3, targetSleepHours = 8, targetSleepQuality = "Good", targetSleepTiming = "10:00 PM", entryTime = DateTime.now()),
    SleepGoal(id = 2, userId = 3, targetSleepHours = 7, targetSleepQuality = "Fair", targetSleepTiming = "11:00 PM", entryTime = DateTime.now())
)

val sleepGoalLogsAndStats = arrayListOf<SleepGoalLogAndStat>(
    SleepGoalLogAndStat(id = 1, userId = 3, sleepGoalId = 1, actualSleepHours = 7, actualSleepQuality = "Good", actualSleepTiming = "10:00 PM",
        sleepInterruptions = 1, sleepPhaseDetails = "Light sleep", sleepQualityIndex = 80, remarks = "Slight interruption", entryTime = DateTime.now()),
    SleepGoalLogAndStat(id = 2, userId = 3, sleepGoalId = 2, actualSleepHours = 6, actualSleepQuality = "Fair", actualSleepTiming = "11:30 PM",
        sleepInterruptions = 2, sleepPhaseDetails = "Deep sleep", sleepQualityIndex = 70, remarks = "Multiple interruptions", entryTime = DateTime.now())
)

// Test Data
val healthGoals = arrayListOf<HealthGoal>(
    HealthGoal(id = 1, userId = 3, healthGoalType = "Weight Loss", targetValue = 5, entryTime = DateTime.now()),
    HealthGoal(id = 2, userId = 3, healthGoalType = "Step Count", targetValue = 10000, entryTime = DateTime.now())
)

val healthGoalLogsAndPerformances = arrayListOf<HealthGoalLogAndPerformance>(
    HealthGoalLogAndPerformance(
        id = 1,
        userId = 3,
        healthGoalId = 1,
        status = "In Progress",
        achievedValue = 3,
        targetReached = false,
        remarks = "Making progress",
        entryTime = DateTime.now()
    ),
    HealthGoalLogAndPerformance(
        id = 2,
        userId = 3,
        healthGoalId = 2,
        status = "Completed",
        achievedValue = 10000,
        targetReached = true,
        remarks = "Goal achieved successfully",
        entryTime = DateTime.now()
    )
)

val dietGoals = arrayListOf<DietGoal>(
    DietGoal(id = 1, userId = 3, dietGoalType = "Weight Loss", targetCalories = 1500, entryTime = DateTime.now()),
    DietGoal(id = 2, userId = 3, dietGoalType = "Maintenance", targetCalories = 2000, entryTime = DateTime.now())
)

val dietGoalLogsAndPerformances = arrayListOf<DietGoalLogAndPerformance>(
    DietGoalLogAndPerformance(
        id = 1,
        userId = 3,
        dietGoalId = 1,
        caloriesConsumed = 1600,
        recommendations = "Reduce 100 calories to meet the target",
        status = "In Progress",
        targetReached = false,
        deficitSurplus = "+100",
        entryTime = DateTime.now()
    ),
    DietGoalLogAndPerformance(
        id = 2,
        userId = 3,
        dietGoalId = 2,
        caloriesConsumed = 2000,
        recommendations = "Maintain current calorie intake",
        status = "Completed",
        targetReached = true,
        deficitSurplus = "0",
        entryTime = DateTime.now()
    )
)

val screenTimeGoals = arrayListOf<ScreenTimeGoal>(
    ScreenTimeGoal(id = 1, userId = 3, targetScreenHours = 4, entryTime = DateTime.now()),
    ScreenTimeGoal(id = 2, userId = 3, targetScreenHours = 5, entryTime = DateTime.now())
)

val screenTimeLogsAndPerformances = arrayListOf<ScreenTimeLogAndPerformance>(
    ScreenTimeLogAndPerformance(
        id = 1,
        userId = 3,
        screenTimeGoalId = 1,
        actualScreenHours = 5,
        targetMet = false,
        extraHours = 1,
        recommendations = "Reduce screen time by 1 hour to meet the target.",
        entryTime = DateTime.now()
    ),
    ScreenTimeLogAndPerformance(
        id = 2,
        userId = 3,
        screenTimeGoalId = 2,
        actualScreenHours = 4,
        targetMet = true,
        extraHours = 0,
        recommendations = "Keep up the good work!",
        entryTime = DateTime.now()
    )
)

//Pouplation Section

fun populateUserTable(): UserDAO {
    SchemaUtils.create(Users)
    val userDAO = UserDAO()
    userDAO.save(users[0])
    userDAO.save(users[1])
    userDAO.save(users[2])
    return userDAO
}

fun populateExerciseScheduleTable(): ExerciseScheduleDAO {
    SchemaUtils.create(ExerciseSchedules)
    val scheduleDAO = ExerciseScheduleDAO()
    scheduleDAO.save(schedulesExercise[0])
    scheduleDAO.save(schedulesExercise[1])
    return scheduleDAO
}
fun populateExerciseLogTable(): ExerciseLogDAO {
    SchemaUtils.create(ExerciseLogs)
    val logDAO = ExerciseLogDAO()
    logDAO.save(logsExercise[0])
    logDAO.save(logsExercise[1])
    return logDAO
}

fun populateExercisePerformanceTable(): ExercisePerformanceTrackingDAO {
    SchemaUtils.create(ExercisePerformanceTrackings)
    val performanceDAO = ExercisePerformanceTrackingDAO()
    performanceDAO.calculateUserPerformance(userId)
    return performanceDAO
}

fun populateSleepGoalTable(): SleepGoalDAO {
    SchemaUtils.create(SleepGoals)
    val sleepGoalDAO = SleepGoalDAO()
    sleepGoalDAO.save(goalsSleep[0])
    sleepGoalDAO.save(goalsSleep[1])
    return sleepGoalDAO
}

fun populateSleepGoalLogAndTable(): SleepGoalLogAndStatsDAO{
    SchemaUtils.create(SleepGoalLogAndStats)
    val sleepGoalLogDAO = SleepGoalLogAndStatsDAO()
    sleepGoalLogDAO.save(sleepGoalLogsAndStats[0])
    sleepGoalLogDAO.save(sleepGoalLogsAndStats[1])
    return sleepGoalLogDAO
}

fun populateHealthGoalTable(): HealthGoalDAO {
    SchemaUtils.create(HealthGoals)
    val healthGoalDAO = HealthGoalDAO()
    healthGoalDAO.save(healthGoals[0])
    healthGoalDAO.save(healthGoals[1])
    return healthGoalDAO
}

fun populateHealthGoalLogAndPerformanceTable(): HealthGoalLogAndPerformanceDAO {
    SchemaUtils.create(HealthGoalLogAndPerformances)
    val healthGoalLogDAO = HealthGoalLogAndPerformanceDAO()
    healthGoalLogDAO.save(healthGoalLogsAndPerformances[0])
    healthGoalLogDAO.save(healthGoalLogsAndPerformances[1])
    return healthGoalLogDAO
}

fun populateDietGoalTable(): DietGoalDAO {
    SchemaUtils.create(DietGoals)
    val dietGoalDAO = DietGoalDAO()
    dietGoalDAO.save(dietGoals[0])
    dietGoalDAO.save(dietGoals[1])
    return dietGoalDAO
}

fun populateDietGoalLogAndPerformanceTable(): DietGoalLogAndPerformanceDAO {
    SchemaUtils.create(DietGoalLogAndPerformances)
    val dietGoalLogDAO = DietGoalLogAndPerformanceDAO()
    dietGoalLogDAO.save(dietGoalLogsAndPerformances[0])
    dietGoalLogDAO.save(dietGoalLogsAndPerformances[1])
    return dietGoalLogDAO
}

fun populateScreenTimeGoalTable(): ScreenTimeGoalDAO {
    SchemaUtils.create(ScreenTimeGoals)
    val screenTimeGoalDAO = ScreenTimeGoalDAO()
    screenTimeGoalDAO.save(screenTimeGoals[0])
    screenTimeGoalDAO.save(screenTimeGoals[1])
    return screenTimeGoalDAO
}

fun populateScreenTimeLogAndPerformanceTable(): ScreenTimeLogAndPerformanceDAO {
    SchemaUtils.create(ScreenTimeLogAndPerformances)
    val screenTimeLogDAO = ScreenTimeLogAndPerformanceDAO()
    screenTimeLogDAO.save(screenTimeLogsAndPerformances[0])
    screenTimeLogDAO.save(screenTimeLogsAndPerformances[1])
    return screenTimeLogDAO
}
