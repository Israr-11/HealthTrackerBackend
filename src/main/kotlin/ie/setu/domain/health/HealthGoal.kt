package ie.setu.domain.health

import org.joda.time.DateTime

data class HealthGoal(
    var id: Int,
    var userId: Int,
    var healthGoalType: String,
    var targetValue: Int,
    var entryTime: DateTime?
)
