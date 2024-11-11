package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

object ForgotPassword : Table("forgot_password") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val resetToken= integer("reset_token")
    val tokenExpiry = datetime("token_expiry")

    override val primaryKey = PrimaryKey(id, name = "PK_ForgotPassword_ID")
}