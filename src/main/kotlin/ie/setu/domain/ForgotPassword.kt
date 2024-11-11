package ie.setu.domain

import org.joda.time.DateTime

data class ForgotPassword (
                 var id: Int,
                 var userId: Int,
                 var resetToken:Int,
                 var tokenExpiry:DateTime,
)