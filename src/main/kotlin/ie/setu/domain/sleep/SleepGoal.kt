package ie.setu.domain.sleep

import org.joda.time.DateTime

data class SleepGoal(
    var id: Int,
    val userId: Int,
    val targetSleepHours: Int,
    val targetSleepQuality: String,
    val targetSleepTiming: String,
    val entryTime: DateTime?
)
