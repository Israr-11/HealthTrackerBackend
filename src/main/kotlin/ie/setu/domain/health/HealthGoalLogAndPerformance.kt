package ie.setu.domain.health

import org.joda.time.DateTime

data class HealthGoalLogAndPerformance(
    var id: Int,
    var userId: Int,
    var healthGoalId: Int,
    var status: String?,
    var achievedValue: Int,
    var targetReached: Boolean?,
    var remarks: String?,
    var entryTime: DateTime?,
    )
