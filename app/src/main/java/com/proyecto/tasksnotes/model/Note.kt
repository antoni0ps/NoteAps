package com.proyecto.tasksnotes.model

class Note {
    var userUid: String? = null
    var noteId: String? = null
    var email : String? = null
    var title: String? = null
    var content: String? = null
    var colorCode : Int? = null

    constructor() {}

    constructor(userUid: String?, noteId: String?,email : String?, title: String?, content: String?, colorCode: Int?) {
        this.userUid = userUid
        this.noteId = noteId
        this.email = email
        this.title = title
        this.content = content
        this.colorCode = colorCode

    }


}