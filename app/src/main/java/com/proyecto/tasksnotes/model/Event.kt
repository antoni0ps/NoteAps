package com.proyecto.tasksnotes.model

data class Event(
    var userUid: String?,
    var eventId: String?,
    var userName: String?,
    var email: String?,
    var title: String?,
    var description: String?,
    var taskDate: String?,
){
    constructor() : this("","","","","","","")
}
