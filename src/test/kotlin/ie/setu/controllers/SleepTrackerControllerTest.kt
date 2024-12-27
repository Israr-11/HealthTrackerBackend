package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.sleep.SleepGoal
import ie.setu.helpers.*
import ie.setu.utils.jsonNodeToObject
import ie.setu.utils.jsonToObject
import kong.unirest.core.HttpResponse
import kong.unirest.core.JsonNode
import kong.unirest.core.Unirest
import kong.unirest.core.json.JSONObject
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SleepTrackerControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class ReadSleepGoals {
        @Test
        fun `get all sleep goals returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/sleep-goal/").asString()
            if (response.status == 200) {
                val sleepGoals: ArrayList<SleepGoal> = jsonToObject(response.body.toString())
                assertNotEquals(0, sleepGoals.size)
            } else {
                assertEquals(404, response.status)
            }
        }
    }

    @Nested
    inner class CreateSleepGoal {

        @Test
        fun `create a sleep goal with correct details returns 201 response`() {

            val addResponse = addSleepGoal(
                userId = 3,
                targetSleepHours = 8,
                targetSleepQuality = "good",
                targetSleepTiming = "night"
            )
            assertEquals(201, addResponse.status)

        }

    }

    @Nested
    inner class UpdateSleepGoals {

        @Test
        fun `updating a sleep goal when it exists returns a 204 response`() {

            val addedResponse = addSleepGoal(
                userId = 3,
                targetSleepHours = 8,
                targetSleepQuality = "good",
                targetSleepTiming = "night"
            )
            assertEquals(201, addedResponse.status)
            val addedGoal: SleepGoal = jsonToObject(addedResponse.body.toString())

            val updatedResponse = updateSleepGoal(
                sleepGoalId = addedGoal.id,
                userId = 3,
                targetSleepHours = 9,
                targetSleepQuality = "excellent",
                targetSleepTiming = "night"
            )
            assertEquals(204, updatedResponse.status)
        }

        @Test
        fun `updating a sleep goal when it doesn't exist returns a 404 response`() {

            val response = updateSleepGoal(
                sleepGoalId = -1,
                userId = 3,
                targetSleepHours = 9,
                targetSleepQuality = "excellent",
                targetSleepTiming = "night"
            )
            assertEquals(404, response.status)
        }
    }

    @Nested
    inner class CreateSleepLog {

        @Test
        fun `create a sleep log and generate stats with correct details returns 201 response`() {

            val addResponse = addSleepLog(

                userId= 3,
            sleepGoalId= 7,
            actualSleepHours=7,
            actualSleepQuality="good",
            actualSleepTiming="night"

            )
            assertEquals(201, addResponse.status)

        }

    }


    @Nested
    inner class GetSleepLogsByUserId {

        @Test
        fun `fetch sleep logs by user id, returns 200 response and data`() {

            val validUserId = 3

            val response = getSleepLogsByUserId(validUserId)

            assertEquals(200, response.status)
        }

        @Test
        fun `fetch sleep logs by non-existent user id, returns 404 response`() {
            val invalidUserId = 609

            val response = getSleepLogsByUserId(invalidUserId)

            assertEquals(404, response.status)
        }
    }


    @Nested
    inner class DeleteSleepLogBySleepGoalId {

        @Test
        fun `delete a sleep log by valid sleep goal id, returns 204 response`() {

            val validSleepGoalId = 7

            val deleteResponse = deleteSleepLogBySleepGoalId(validSleepGoalId)

            assertEquals(204, deleteResponse.status)
        }

        @Test
        fun `delete a sleep log by non-existent sleep goal id, returns 404 response`() {
            val invalidSleepGoalId = 546

            val deleteResponse = deleteSleepLogBySleepGoalId(invalidSleepGoalId)

            assertEquals(404, deleteResponse.status)
        }
    }




    //Helpers
    private fun addSleepGoal(
        userId: Int,
        targetSleepHours: Int?,
        targetSleepQuality: String?,
        targetSleepTiming: String?
    ): HttpResponse<JsonNode> {
        val requestBody = JSONObject().apply {
            put("userId", userId)
            targetSleepHours?.let { put("targetSleepHours", it) }
            targetSleepQuality?.let { put("targetSleepQuality", it) }
            targetSleepTiming?.let { put("targetSleepTiming", it) }
        }

        return Unirest.post("$origin/api/sleep-goal")
            .header("Content-Type", "application/json")
            .body(requestBody.toString())
            .asJson()
    }

    private fun updateSleepGoal(
        sleepGoalId: Int,
        userId: Int,
        targetSleepHours: Int,
        targetSleepQuality: String,
        targetSleepTiming: String
    ): HttpResponse<JsonNode> {
        val requestBody = JSONObject().apply {
            put("userId", userId)
            put("targetSleepHours", targetSleepHours)
            put("targetSleepQuality", targetSleepQuality)
            put("targetSleepTiming", targetSleepTiming)
        }

        return Unirest.patch("$origin/api/sleep-goal/$sleepGoalId")
            .header("Content-Type", "application/json")
            .body(requestBody.toString())
            .asJson()
    }

    private fun addSleepLog(
        userId: Int,
        sleepGoalId: Int?,
        actualSleepHours: Int?,
        actualSleepQuality: String?,
        actualSleepTiming: String?
    ): HttpResponse<JsonNode> {
        val requestBody = JSONObject().apply {
            put("userId", userId)
            sleepGoalId?.let { put("sleepGoalId", it) }
            actualSleepHours?.let { put("actualSleepHours", it) }
            actualSleepQuality?.let { put("actualSleepQuality", it) }
            actualSleepTiming?.let { put("actualSleepTiming", it) }
        }

        return Unirest.post("$origin/api/sleep-goal-and-stats")
            .header("Content-Type", "application/json")
            .body(requestBody.toString())
            .asJson()
    }

    private fun getSleepLogsByUserId(userId: Int): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/sleep-goal-and-stats/$userId")
            .header("Content-Type", "application/json")
            .asJson()
    }

    private fun deleteSleepLogBySleepGoalId(sleepGoalId: Int): HttpResponse<JsonNode> {
        return Unirest.delete("$origin/api/sleep-goal-and-stats/$sleepGoalId")
            .header("Content-Type", "application/json")
            .asJson()
    }


}
