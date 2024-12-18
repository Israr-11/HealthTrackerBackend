package ie.setu.domain.walk

import org.joda.time.DateTime

data class WalkLogAndStat(
    var id: Int,
    val userId: Int,
    val walkGoalId: Int,
    val actualSteps: Int,
    val targetMet: Boolean?,
    val extraSteps: Int?,
    val walkQuality: String?,
    val recommendations: String?,
    val entryTime: DateTime?
)
