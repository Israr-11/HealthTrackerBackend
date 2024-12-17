package ie.setu.domain.mentalhealth

import org.joda.time.DateTime

data class MentalHealthLogAndPerformance(
    var id: Int,
    val userId: Int,
    val mentalHealthGoalId: Int,
    val moodScore: Int,
    val stressLevel: Int,
    val targetMet: Boolean?,
    val recommendations: String?,
    val entryTime: DateTime?
)
