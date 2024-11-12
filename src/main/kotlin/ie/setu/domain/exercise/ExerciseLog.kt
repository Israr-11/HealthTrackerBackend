package ie.setu.domain.exercise

import org.joda.time.DateTime

data class ExerciseLog(
    var id: Int,
    var userId: Int,
    var exerciseId: Int,
    var status: String,
    var entryTime: DateTime?,
    )
