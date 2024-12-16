package ie.setu.domain.water

import org.joda.time.DateTime

data class WaterGoal(
    var id: Int,
    val userId: Int,
    val waterTarget: Int,
    val entryTime: DateTime?
)
