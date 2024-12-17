package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.mentalhealth.MentalHealthGoal
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
class MentalHealthTrackerControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class ReadMentalHealthGoals {
        @Test
        fun `get all mental health goals returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/mental-health-goal/").asString()
            if (response.status == 200) {
                val mentalHealthGoals: ArrayList<MentalHealthGoal> = jsonToObject(response.body.toString())
                assertNotEquals(0, mentalHealthGoals.size)
            } else {
                assertEquals(404, response.status)
            }
        }
    }

    @Nested
    inner class CreateMentalHealthGoal {

        @Test
        fun `create a mental health goal with correct details returns 201 response`() {

            val addResponse = addMentalHealthGoal(
                userId = 3,
                targetMoodScore = 8
            )
            assertEquals(201, addResponse.status)

        }

    }

    @Nested
    inner class CreateMentalHealthLog {

        @Test
        fun `create a mental health log and generate stats with correct details returns 201 response`() {

            val addResponse = createMentalHealthLog(
                userId = 3,
                mentalHealthGoalId = 1,
                moodScore = 7
            )
            assertEquals(201, addResponse.status)

        }

    }

    // Helpers
    private fun addMentalHealthGoal(
        userId: Int,
        targetMoodScore: Int?
    ): HttpResponse<JsonNode> {
        val requestBody = JSONObject().apply {
            put("userId", userId)
            targetMoodScore?.let { put("targetMoodScore", it) }
        }

        return Unirest.post("$origin/api/mental-health-goal/")
            .header("Content-Type", "application/json")
            .body(requestBody.toString())
            .asJson()
    }

    private fun createMentalHealthLog(
        userId: Int,
        mentalHealthGoalId: Int?,
        moodScore: Int?
    ): HttpResponse<JsonNode> {
        val requestBody = JSONObject().apply {
            put("userId", userId)
            mentalHealthGoalId?.let { put("mentalHealthGoalId", it) }
            moodScore?.let { put("moodScore", it) }
        }

        return Unirest.post("$origin/api/mental-health-goal-and-log")
            .header("Content-Type", "application/json")
            .body(requestBody.toString())
            .asJson()
    }
}
