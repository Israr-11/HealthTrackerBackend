package ie.setu.domain.diet

import org.joda.time.DateTime

data class DietGoal(
    var id: Int,
    var userId: Int,
    var dietGoalType: String,
    var targetCalories: Int,
    var entryTime: DateTime?
)
