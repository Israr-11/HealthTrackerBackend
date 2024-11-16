package ie.setu.domain.diet

import org.joda.time.DateTime

data class DietGoalLogAndPerformance(
    var id: Int,
    var userId: Int,
    var dietGoalId: Int,
    var caloriesConsumed: Int,
    var recommendations: String?,
    var status: String?,
    var targetReached: Boolean?,
    var deficitSurplus: String?,
    var entryTime: DateTime?,
    )
