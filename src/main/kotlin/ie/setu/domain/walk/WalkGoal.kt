package ie.setu.domain.walk

import org.joda.time.DateTime

data class WalkGoal(
    var id: Int,
    val userId: Int,
    val targetSteps: Int,
    val uphill: Boolean,
    val entryTime: DateTime?
)
