package com.example.eslatmalar

data class ToDoModel(
    val toDoTitle: String,
    val toDoDescription: String,
    val toDoThisDay: String,
    val toDoPostedTime: String,
    var isMainToDo: Boolean = false
)
