package ie.setu.domain.exercise

import org.joda.time.DateTime

data class ExercisePerformanceTracking(
    var id: Int,
    var userPerId: Int,
    var performanceStatus: String,
    var achievedCount:Int,
    var missCount: Int,
    var checkedAt: DateTime?
)