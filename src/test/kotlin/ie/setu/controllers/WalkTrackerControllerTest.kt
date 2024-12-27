package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.walk.WalkGoal
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
class WalkTrackerControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class ReadWalkGoals {
        @Test
        fun `get all walk goals returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/walk-goal/").asString()
            if (response.status == 200) {
                val walkGoals: ArrayList<WalkGoal> = jsonToObject(response.body.toString())
                assertNotEquals(0, walkGoals.size)
            } else {
                assertEquals(404, response.status)
            }
        }
    }

    @Nested
    inner class CreateWalkGoal {

        @Test
        fun `create a walk goal with correct details returns 201 response`() {

            val addResponse = addWalkGoal(
                userId = 3,
                targetSteps = 10000,
                uphill = true
            )
            assertEquals(201, addResponse.status)

        }

    }

    @Nested
    inner class CreateWalkLog {

        @Test
        fun `create a walk log and generate stats with correct details returns 201 response`() {

            val addResponse = createWalkLog(
                userId = 3,
                walkGoalId = 11,
                actualSteps = 10500
            )
            assertEquals(201, addResponse.status)

        }

    }

    // Helpers
    private fun addWalkGoal(
        userId: Int,
        targetSteps: Int?,
        uphill: Boolean?
    ): HttpResponse<JsonNode> {
        val requestBody = JSONObject().apply {
            put("userId", userId)
            targetSteps?.let { put("targetSteps", it) }
            uphill?.let { put("uphill", it) }
        }

        return Unirest.post("$origin/api/walk-goal/")
            .header("Content-Type", "application/json")
            .body(requestBody.toString())
            .asJson()
    }

    private fun createWalkLog(
        userId: Int,
        walkGoalId: Int?,
        actualSteps: Int?
    ): HttpResponse<JsonNode> {
        val requestBody = JSONObject().apply {
            put("userId", userId)
            walkGoalId?.let { put("walkGoalId", it) }
            actualSteps?.let { put("actualSteps", it) }
        }

        return Unirest.post("$origin/api/walk-goal-and-log")
            .header("Content-Type", "application/json")
            .body(requestBody.toString())
            .asJson()
    }
}
