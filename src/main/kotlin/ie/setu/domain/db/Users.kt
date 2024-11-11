package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object Users : Table("users") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val email = varchar("email", 255)
    //val password = varchar("password", 255)
    //val createdAt = datetime("created_at")

    override val primaryKey = PrimaryKey(id, name = "PK_Users_ID")
}