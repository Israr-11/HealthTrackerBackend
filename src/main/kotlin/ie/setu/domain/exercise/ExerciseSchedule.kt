package ie.setu.domain.exercise

import org.joda.time.DateTime

data class ExerciseSchedule(
    var id: Int,
    var userId: Int,
    var exerciseType: String,
    var duration: Int,
    var entryTime: DateTime?
)