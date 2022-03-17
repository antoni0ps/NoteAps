package com.proyecto.tasksnotes.model

import android.widget.Toast

class Task {
    var userUid: String? = null
    var taskId: String? = null
    var userName: String? = null
    var email: String? = null
    var registerDate: String? = null
    var title: String? = null
    var description: String? = null
    var taskDate: String? = null
    var status: String? = null

    constructor()

    constructor(userUid: String?, taskId: String?, user: String?, email: String?, registryDate: String?, title: String?, description: String?, finishDate: String?, status: String?) {
        this.userUid = userUid
        this.taskId = taskId
        this.userName = user
        this.email = email
        this.registerDate = registryDate
        this.title = title
        this.description = description
        this.taskDate = finishDate
        this.status = status
    }

}