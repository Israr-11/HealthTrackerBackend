package ie.setu.config

import ie.setu.controllers.HealthTrackerController
import ie.setu.controllers.HealthGoalTrackerController
import ie.setu.controllers.ExerciseTrackerController
import ie.setu.controllers.DietGoalTrackerController
import ie.setu.utils.jsonObjectMapper
import io.javalin.Javalin
import io.javalin.json.JavalinJackson
import io.javalin.vue.VueComponent

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
        app.get("/api/users", HealthTrackerController::getAllUsers)
        app.get("/api/users/{user-id}", HealthTrackerController::getUserByUserId)

        app.post("/api/users", HealthTrackerController::addUser)
        app.delete("/api/users/{user-id}", HealthTrackerController::deleteUser)
        app.patch("/api/users/{user-id}", HealthTrackerController::updateUser)

        app.get("/api/users/email/{email}", HealthTrackerController::getUserByEmail)
        app.get("/api/users/{user-id}/activities", HealthTrackerController::getActivitiesByUserId)
        app.delete("/api/users/{user-id}/activities", HealthTrackerController::deleteActivityByUserId)

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
        app.get("/api/health-goal", HealthGoalTrackerController::getAllHealthGoal)
        app.post("/api/health-goal",HealthGoalTrackerController::addHealthGoal)
        app.patch("/api/health-goal/{health-goal-id}", HealthGoalTrackerController::updateHealthGoalByGoalId)

        app.get("/api/health-goal-and-log/{user-id}", HealthGoalTrackerController::getHealthPerformanceByUserId)
        app.post("/api/health-goal-and-log", HealthGoalTrackerController::addHealthLogAndPerformance)
        app.delete("/api/health-goal-and-log/{health_goal_id}", HealthGoalTrackerController::deleteByUserId)

        //---------------
        // Feature 3: Diet Goal, Log and Performance API paths
        //---------------
        app.get("/api/diet-goal", DietGoalTrackerController::getAllDietGoal)
        app.post("/api/diet-goal",DietGoalTrackerController::addDietGoal)
        app.patch("/api/diet-goal/{diet-goal-id}", DietGoalTrackerController::updateDietGoalByGoalId)

        app.get("/api/diet-goal-and-log/{user-id}", DietGoalTrackerController::getDietPerformanceByUserId)
        app.post("/api/diet-goal-and-log", DietGoalTrackerController::addDietLogAndPerformance)
        app.delete("/api/diet-goal-and-log/{diet_goal_id}", DietGoalTrackerController::deleteByUserId)
        //---------------------
        // Activities API paths
        //---------------------
        app.get("/api/activities", HealthTrackerController::getAllActivities)
        app.post("/api/activities", HealthTrackerController::addActivity)

        app.delete("/api/activities/{activity-id}", HealthTrackerController::deleteActivityByActivityId)
        app.patch("/api/activities/{activity-id}", HealthTrackerController::updateActivity)

        app.get("/api/activities/{activity-id}", HealthTrackerController::getActivitiesByActivityId)


        // The @routeComponent that we added in layout.html earlier will be replaced
        // by the String inside the VueComponent. This means a call to / will load
        // the layout and display our <home-page> component.
        app.get("/", VueComponent("<home-page></home-page>"))
        app.get("/users", VueComponent("<user-overview></user-overview>"))
        app.get("/users/{user-id}", VueComponent("<user-profile></user-profile>"))
        app.get("/users/{user-id}/activities", VueComponent("<user-activity-overview></user-activity-overview>"))
    }

    private fun getRemoteAssignedPort(): Int {
        val remotePort = System.getenv("PORT")
        return if (remotePort != null) {
            Integer.parseInt(remotePort)
        } else 7001
    }
}