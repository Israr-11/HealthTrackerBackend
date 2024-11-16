package ie.setu.domain.screentime

import org.joda.time.DateTime

data class ScreenTimeGoal(
    var id: Int,
    val userId: Int,
    val targetScreenHours: Int,
    val entryTime: DateTime?
)