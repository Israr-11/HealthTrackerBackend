package ie.setu.config

import ie.setu.controllers.UserController
import ie.setu.controllers.HealthTrackerController
import ie.setu.controllers.ExerciseTrackerController
import ie.setu.controllers.DietTrackerController
import ie.setu.controllers.SleepTrackerController
import ie.setu.controllers.ScreenTimeTrackerController
import ie.setu.utils.jsonObjectMapper
import io.javalin.Javalin
import io.javalin.json.JavalinJackson

class JavalinConfig {

    val app = Javalin.create{
        //added this jsonMapper for our integration tests - serialise objects to json
        it.jsonMapper(JavalinJackson(jsonObjectMapper()))
        it.staticFiles.enableWebjars()
        it.vue.vueInstanceNameInJs = "app" // only required for Vue 3, is defined in layout.html
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
        app.get("/api/users", UserController::getAllUsers)
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

        app.get("/api/exercise-log", ExerciseTrackerController::getAllExerciseLogs)
        app.post("/api/exercise-log",ExerciseTrackerController::addExerciseLog )
        app.delete("/api/exercise-log/{exercise-log-id}",ExerciseTrackerController::deleteEexerciseLog)

        app.get("/api/exercise-performance/{user-id}", ExerciseTrackerController::calculateAndSaveUserPerformance)
        app.get("/api/{user-per-id}/exercise-performance/", ExerciseTrackerController::getPerformanceByUserPerId)
        app.delete("/api/exercise-performance/{user-per-id}", ExerciseTrackerController::deleteByUserPerId)

        //---------------
        // Feature 2: Health Goal, Log and Performance API paths
        //---------------
        app.get("/api/health-goal", HealthTrackerController::getAllHealthGoal)
        app.post("/api/health-goal",HealthTrackerController::addHealthGoal)
        app.patch("/api/health-goal/{health-goal-id}", HealthTrackerController::updateHealthGoalByGoalId)

        app.get("/api/health-goal-and-log/{user-id}", HealthTrackerController::getHealthPerformanceByUserId)
        app.post("/api/health-goal-and-log", HealthTrackerController::addHealthLogAndPerformance)
        app.delete("/api/health-goal-and-log/{health_goal_id}", HealthTrackerController::deleteByUserId)

        //---------------
        // Feature 3: Diet Goal, Log and Performance API paths
        //---------------
        app.get("/api/diet-goal", DietTrackerController::getAllDietGoal)
        app.post("/api/diet-goal",DietTrackerController::addDietGoal)
        app.patch("/api/diet-goal/{diet-goal-id}", DietTrackerController::updateDietGoalByGoalId)

        app.get("/api/diet-goal-and-log/{user-id}", DietTrackerController::getDietPerformanceByUserId)
        app.post("/api/diet-goal-and-log", DietTrackerController::addDietLogAndPerformance)
        app.delete("/api/diet-goal-and-log/{diet_goal_id}", DietTrackerController::deleteByUserId)

        //---------------
        // Feature 4: Sleep Goal, Log and Stats API paths
        //---------------
        app.get("/api/sleep-goal", SleepTrackerController::getAllSleepGoal)
        app.post("/api/sleep-goal",SleepTrackerController::addSleepGoal)
        app.patch("/api/sleep-goal/{sleep-goal-id}", SleepTrackerController::updateSleepGoalByGoalId)

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

        app.get("/api/screen-time-goal-and-log/{user-id}", ScreenTimeTrackerController::getScreenTimePerformanceByUserId)
        app.post("/api/screen-time-goal-and-log", ScreenTimeTrackerController::addScreenTimeLogAndPerformance)
        app.delete("/api/screen-time-goal-and-log/{screen_time_goal_id}", ScreenTimeTrackerController::deleteByScreenTimeGoalId)

    }

    private fun getRemoteAssignedPort(): Int {
        val remotePort = System.getenv("PORT")
        return if (remotePort != null) {
            Integer.parseInt(remotePort)
        } else 7001
    }
}