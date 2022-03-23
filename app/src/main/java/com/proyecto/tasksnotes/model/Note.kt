package com.proyecto.tasksnotes.model

data class Note(
    var noteId: String?,
    var email: String?,
    var title: String?,
    var content: String?,
    var colorCode: Int?
) {
    constructor() : this("", "", "", "", 0)
}




