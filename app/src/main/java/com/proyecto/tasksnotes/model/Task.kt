package com.proyecto.tasksnotes.model

data class Task(
    var taskId: String?,
    var userName: String?,
    var email: String?,
    var registerDate: String?,
    var title: String?,
    var description: String?,
    var taskDate: String?,
    var status: String?
) {
    constructor() : this("", "", "", "", "", "", "", "")
}