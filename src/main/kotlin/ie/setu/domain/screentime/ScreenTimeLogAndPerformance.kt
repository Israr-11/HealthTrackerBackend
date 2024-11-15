package ie.setu.domain.screentime

import org.joda.time.DateTime

data class ScreenTimeLogAndPerformance(
    var id: Int,
    val userId: Int,
    val screenTimeGoalId: Int,
    val actualScreenHours: Int,
    val targetMet: Boolean?,
    val extraHours: Int?,
    val recommendations: String?,
    val entryTime: DateTime?
)
