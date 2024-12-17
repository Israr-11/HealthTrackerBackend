package ie.setu.domain.mentalhealth

import org.joda.time.DateTime

data class MentalHealthGoal(
    var id: Int,
    val userId: Int,
    val targetMoodScore: Int,
    val entryTime: DateTime?
)
