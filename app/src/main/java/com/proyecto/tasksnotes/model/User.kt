package com.proyecto.tasksnotes.model

data class User(
    var uid : String?,
    var name: String?,
    var surname: String?,
    var email: String?

) {
    constructor() : this("", "", "", "")
}

