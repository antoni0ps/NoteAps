package com.proyecto.tasksnotes.model

data class Event(
    var userUid: String?,
    var eventId: String?,
    var userName: String?,
    var email: String?,
    var title: String?,
    var description: String?,
    var event_date: String?,
    var event_time: String
){
    constructor() : this("","","","","","","","")
}
