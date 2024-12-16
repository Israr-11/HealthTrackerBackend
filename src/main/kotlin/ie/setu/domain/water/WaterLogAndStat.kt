package ie.setu.domain.water

import org.joda.time.DateTime

data class WaterLogAndStat(
    var id: Int,
    val userId: Int,
    val waterGoalId: Int,
    val actualWaterIntake: Int,
    val targetMet: Boolean?,
    val shortfall: Int?,
    val recommendations: String?,
    val entryTime: DateTime?
)
