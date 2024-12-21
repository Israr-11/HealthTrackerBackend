package ie.setu.config

import ie.setu.controllers.*
import ie.setu.utils.jsonObjectMapper
import io.javalin.Javalin
import io.javalin.json.JavalinJackson

class JavalinConfig {

    val app = Javalin.create{
        //added this jsonMapper for our integration tests - serialise objects to json
        it.jsonMapper(JavalinJackson(jsonObjectMapper()))
        it.bundledPlugins.enableCors { cors ->
            cors.addRule { crs ->
                crs.allowHost("*")
                crs.allowCredentials = true
            }
        }
    }.apply {
        exception(Exception::class.java) { e, _ -> e.printStackTrace() }
        error(404) { ctx -> ctx.json("404 : Not Found") }
    }

    fun startJavalinService(): Javalin {
        app.start(getRemoteAssignedPort())
        registerRoutes(app)
        return app
    }

    fun getJavalinService(): Javalin {
        registerRoutes(app)
        return app
    }

    private fun registerRoutes(app: Javalin) {
        //---------------
        // User API paths
        //---------------
        app.get("/api/users/{email}", UserController::findByEmail)
        app.get("/api/users/{user-id}", UserController::getUserByUserId)
        app.post("/api/users", UserController::addUser)
        app.delete("/api/users/{user-id}", UserController::deleteUser)
        app.patch("/api/users/{user-id}", UserController::updateUser)
        //---------------
        // Feature 1: Exercise API paths
        //---------------
        app.get("/api/exercise-schedule", ExerciseTrackerController::getAllExerciseSchedules)
        app.post("/api/exercise-schedule",ExerciseTrackerController::addExerciseSchedule )
        app.delete("/api/exercise-schedule/{exercise-schedule-id}",ExerciseTrackerController::deleteExerciseSchedule)
        app.get("/api/exercise-schedule/{user-id}", ExerciseTrackerController::getExerciseScheduleByUserId)

        app.get("/api/exercise-log", ExerciseTrackerController::getAllExerciseLogs)
        app.post("/api/exercise-log",ExerciseTrackerController::addExerciseLog )
        app.delete("/api/exercise-log/{exercise-log-id}",ExerciseTrackerController::deleteEexerciseLog)
        app.get("/api/exercise-log/{user-id}", ExerciseTrackerController::getExerciseLogsByUserId)

        app.get("/api/exercise-performance/{user-id}", ExerciseTrackerController::calculateAndSaveUserPerformance)
        app.get("/api/{user-per-id}/exercise-performance/", ExerciseTrackerController::getPerformanceByUserPerId)
        app.delete("/api/exercise-performance/{user-per-id}", ExerciseTrackerController::deleteByUserPerId)

        //---------------
        // Feature 2: Health Goal, Log and Performance API paths
        //---------------
        app.get("/api/health-goal", HealthTrackerController::getAllHealthGoal)
        app.post("/api/health-goal",HealthTrackerController::addHealthGoal)
        app.patch("/api/health-goal/{health-goal-id}", HealthTrackerController::updateHealthGoalByGoalId)
        app.get("/api/health-goal/{user-id}", HealthTrackerController::getHealthGoalsByUserId)

        app.get("/api/health-goal-and-log/{user-id}", HealthTrackerController::getHealthPerformanceByUserId)
        app.post("/api/health-goal-and-log", HealthTrackerController::addHealthLogAndPerformance)
        app.delete("/api/health-goal-and-log/{health_goal_id}", HealthTrackerController::deleteByUserId)

        //---------------
        // Feature 3: Diet Goal, Log and Performance API paths
        //---------------
        app.get("/api/diet-goal", DietTrackerController::getAllDietGoal)
        app.post("/api/diet-goal",DietTrackerController::addDietGoal)
        app.patch("/api/diet-goal/{diet-goal-id}", DietTrackerController::updateDietGoalByGoalId)
        app.get("/api/diet-goal/{user-id}", DietTrackerController::getDietGoalsByUserId)

        app.get("/api/diet-goal-and-log/{user-id}", DietTrackerController::getDietPerformanceByUserId)
        app.post("/api/diet-goal-and-log", DietTrackerController::addDietLogAndPerformance)
        app.delete("/api/diet-goal-and-log/{diet_goal_id}", DietTrackerController::deleteByUserId)

        //---------------
        // Feature 4: Sleep Goal, Log and Stats API paths
        //---------------
        app.get("/api/sleep-goal", SleepTrackerController::getAllSleepGoal)
        app.post("/api/sleep-goal",SleepTrackerController::addSleepGoal)
        app.patch("/api/sleep-goal/{sleep-goal-id}", SleepTrackerController::updateSleepGoalByGoalId)
        app.get("/api/sleep-goal/{user-id}", SleepTrackerController::getSleepGoalByUserId)

        app.get("/api/sleep-goal-and-stats/{user-id}", SleepTrackerController::getSleepLogAndStatByUserId)
        app.post("/api/sleep-goal-and-stats", SleepTrackerController::addSleepLogAndStat)
        app.delete("/api/sleep-goal-and-stats/{sleep_goal_id}", SleepTrackerController::deleteByUserId)
        //---------------------
        //---------------
        // Feature 5: Screen Time Goal, Log and Performance API paths
        //---------------
        app.post("/api/screen-time-goal", ScreenTimeTrackerController::addScreenTimeGoal)
        app.get("/api/screen-time-goal",ScreenTimeTrackerController::getAllScreenTimeGoal)
        app.patch("/api/screen-time-goal/{screen-time-goal-id}", ScreenTimeTrackerController::updateScreenTimeGoalByGoalId)
        app.get("/api/screen-time-goal/{user-id}", ScreenTimeTrackerController::getScreenTimeGoalsByUserId)

        app.get("/api/screen-time-goal-and-log/{user-id}", ScreenTimeTrackerController::getScreenTimePerformanceByUserId)
        app.post("/api/screen-time-goal-and-log", ScreenTimeTrackerController::addScreenTimeLogAndPerformance)
        app.delete("/api/screen-time-goal-and-log/{screen_time_goal_id}", ScreenTimeTrackerController::deleteByScreenTimeGoalId)
        //---------------------
        //---------------------
        // Feature 6: Water Goal, Log and Performance API paths
        //---------------
        app.post("/api/water-goal", WaterTrackerController::addWaterGoal)
        app.get("/api/water-goal",WaterTrackerController::getAllWaterGoal)
        app.patch("/api/water-goal/{water-goal-id}", WaterTrackerController::updateWaterGoalByGoalId)
        app.get("/api/water-goal/{user-id}", WaterTrackerController::getWaterGoalByUserId)

        app.get("/api/water-goal-and-log/{user-id}", WaterTrackerController::getWaterPerformanceByUserId)
        app.post("/api/water-goal-and-log", WaterTrackerController::addWaterLogAndPerformance)
        app.delete("/api/water-goal-and-log/{water_goal_id}", WaterTrackerController::deleteByWaterGoalId)
        //---------------------
        //---------------------

        // Feature 7: Mental Health Goal, Log and Performance API paths
        //---------------
        app.post("/api/mental-health-goal", MentalHealthTrackerController::addMentalHealthGoal)
        app.get("/api/mental-health-goal", MentalHealthTrackerController::getAllMentalHealthGoals)
        app.patch("/api/mental-health-goal/{mental-health-goal-id}", MentalHealthTrackerController::updateMentalHealthGoalByGoalId)
        app.get("/api/mental-health-goal/{user-id}", MentalHealthTrackerController::getMentalHealthGoalByUserId)

        app.get("/api/mental-health-goal-and-log/{user-id}", MentalHealthTrackerController::getMentalHealthPerformanceByUserId)
        app.post("/api/mental-health-goal-and-log", MentalHealthTrackerController::addMentalHealthLogAndPerformance)
        app.delete("/api/mental-health-goal-and-log/{mental_health_goal_id}", MentalHealthTrackerController::deleteByMentalHealthGoalId)
        //---------------------
        //---------------------
        // Feature 8: Walk Goal, Log and Stats API paths
        //---------------
        app.post("/api/walk-goal", WalkTrackerController::addWalkGoal)
        app.get("/api/walk-goal", WalkTrackerController::getAllWalkGoals)
        app.patch("/api/walk-goal/{walk-goal-id}", WalkTrackerController::updateWalkGoalByGoalId)
        app.get("/api/walk-goal/{user-id}", WalkTrackerController::getWalkGoalByUserId)

        app.get("/api/walk-goal-and-log/{user-id}", WalkTrackerController::getWalkPerformanceByUserId)
        app.post("/api/walk-goal-and-log", WalkTrackerController::addWalkLogAndPerformance)
        app.delete("/api/walk-goal-and-log/{walk_goal_id}", WalkTrackerController::deleteByWalkGoalId)
        //---------------------
        //---------------------
    }

    private fun getRemoteAssignedPort(): Int {
        val remotePort = System.getenv("PORT")
        return if (remotePort != null) {
            Integer.parseInt(remotePort)
        } else 7001
    }
}