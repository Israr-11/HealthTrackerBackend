package ie.setu.domain.sleep

import org.joda.time.DateTime

data class SleepGoalLogAndStat(
    var id: Int,
    val userId: Int,
    val sleepGoalId: Int,
    val actualSleepHours: Int,
    val actualSleepQuality: String,
    val actualSleepTiming: String,
    val sleepInterruptions: Int?,
    val sleepPhaseDetails: String?,
    val sleepQualityIndex: Int?,
    val remarks: String?,
    val entryTime: DateTime?,
    )
