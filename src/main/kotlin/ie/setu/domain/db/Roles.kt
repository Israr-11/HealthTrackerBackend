package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

object Roles : Table("roles") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val role= varchar("role", 50)
    val permissions = text("permissions")

    override val primaryKey = PrimaryKey(id, name = "PK_Roles_ID")
}