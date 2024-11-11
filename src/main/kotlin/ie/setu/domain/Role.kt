package ie.setu.domain

data class Role (var id: Int,
                 var userId: Int,
                 var role:String,
                 var permissions:Array<String>,
)