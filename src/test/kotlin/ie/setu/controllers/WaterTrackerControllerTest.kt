package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.water.WaterGoal
import ie.setu.domain.water.WaterLogAndStat
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
class WaterTrackerControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class ReadWaterGoals {
        @Test
        fun `get all water goals returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/water-goal/").asString()
            if (response.status == 200) {
                val waterGoals: ArrayList<WaterGoal> = jsonToObject(response.body.toString())
                assertNotEquals(0, waterGoals.size)
            } else {
                assertEquals(404, response.status)
            }
        }
    }

    @Nested
    inner class CreateWaterGoal {

        @Test
        fun `create a water goal with correct details returns 201 response`() {

            val addResponse = addWaterGoal(
                userId = 3,
                waterTarget = 3
            )
            assertEquals(201, addResponse.status)

        }
    }

    @Nested
    inner class CreateWaterLog {

        @Test
        fun `create a water log and generate stats with correct details returns 201 response`() {

            val addResponse = createWaterLog(
                userId = 3,
                waterGoalId = 1,
                actualWaterIntake = 3,
            )
            assertEquals(201, addResponse.status)

        }
    }

    // Helpers
    private fun addWaterGoal(
        userId: Int,
        waterTarget: Int?
    ): HttpResponse<JsonNode> {
        val requestBody = JSONObject().apply {
            put("userId", userId)
            waterTarget?.let { put("waterTarget", it) }
        }

        return Unirest.post("$origin/api/water-goal/")
            .header("Content-Type", "application/json")
            .body(requestBody.toString())
            .asJson()
    }

    private fun createWaterLog(
        userId: Int,
        waterGoalId: Int?,
        actualWaterIntake: Int?,
    ): HttpResponse<JsonNode> {
        val requestBody = JSONObject().apply {
            put("userId", userId)
            waterGoalId?.let { put("waterGoalId", it) }
            actualWaterIntake?.let { put("actualWaterIntake", it) }
        }

        return Unirest.post("$origin/api/water-goal-and-log/")
            .header("Content-Type", "application/json")
            .body(requestBody.toString())
            .asJson()
    }
}
