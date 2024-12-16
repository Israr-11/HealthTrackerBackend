package ie.setu.domain.db.water

import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object WaterGoals: Table("water_goals") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val waterTarget  = integer("water_target")
    val entryTime = datetime("entry_time")

    override val primaryKey = PrimaryKey(id, name = "PK_WaterGoals_ID")
}
