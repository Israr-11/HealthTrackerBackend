package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.screentime.ScreenTimeGoal
import ie.setu.helpers.*
import ie.setu.utils.jsonToObject
import kong.unirest.core.HttpResponse
import kong.unirest.core.JsonNode
import kong.unirest.core.Unirest
import kong.unirest.core.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScreenTimeTrackerControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class ReadScreenTimeGoals {
        @Test
        fun `get all screen time goals returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/screen-time-goal/").asString()
            if (response.status == 200) {
                val screenGoals: ArrayList<ScreenTimeGoal> = jsonToObject(response.body.toString())
                assertNotEquals(0, screenGoals.size)
            } else {
                assertEquals(404, response.status)
            }
        }
    }

    @Nested
    inner class CreateSleepGoal {

        @Test
        fun `create a sleep goal with correct details returns 201 response`() {

            val addResponse = addScreenTimeGoal(
                userId = 3,
                targetScreenHours = 8,
            )
            assertEquals(201, addResponse.status)

        }

    }


    @Nested
    inner class CreateScreenTimeLog {

        @Test
        fun `create a sleep log and generate stats with correct details returns 201 response`() {

            val addResponse = CreateScreenTimeLog(

                userId= 3,
                screenTimeGoalId=1,
                actualScreenHours=4

            )
            assertEquals(201, addResponse.status)

        }

    }



    //Helpers
    private fun addScreenTimeGoal(
        userId: Int,
        targetScreenHours: Int?,

        ): HttpResponse<JsonNode> {
        val requestBody = JSONObject().apply {
            put("userId", userId)
            targetScreenHours?.let { put("targetScreenHours", it) }
        }


    return Unirest.post("$origin/api/screen-time-goal/")
    .header ("Content-Type", "application/json")
    .body(requestBody.toString())
    .asJson()
}
    private fun CreateScreenTimeLog(
        userId: Int,
        screenTimeGoalId: Int?,
        actualScreenHours: Int?,
    ): HttpResponse<JsonNode> {
        val requestBody = JSONObject().apply {
            put("userId", userId)
            screenTimeGoalId?.let { put("screenTimeGoalId", it) }
            actualScreenHours?.let { put("actualScreenHours", it) }

        }

        return Unirest.post("$origin/api/screen-time-goal-and-log")
            .header("Content-Type", "application/json")
            .body(requestBody.toString())
            .asJson()
    }

}