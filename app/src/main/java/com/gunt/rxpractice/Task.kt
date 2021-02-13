package com.gunt.rxpractice

data class Task(
    val description: String,
    val isComplete: Boolean,
    val priority: Int
)

fun createTasksList(): MutableList<Task> {
    return mutableListOf(
        Task("Take out the trash", true, 3),
        Task("Walk the dog", false, 2),
        Task("Walk the dog", false, 2),
        Task("Make my bed", true, 1),
        Task("Unload the dishwasher", false, 0)
    )
}