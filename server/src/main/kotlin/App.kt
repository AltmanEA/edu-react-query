package ru.altmanea.eduReactQuery

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import routes.student
import ru.altmanea.eduReactQuery.model.Lesson
import ru.altmanea.eduReactQuery.model.Student
import ru.altmanea.eduReactQuery.routes.lesson

const val lessonsPath = "/lessons"
const val studentsPath = "/students"

fun main() {
    embeddedServer(
        Netty,
        port = 8000,
        host = "127.0.0.1",
        watchPaths = listOf("classes", "resources")
    ) {
        install(CORS) {
            anyHost()
            method(HttpMethod.Options)
            method(HttpMethod.Put)
            method(HttpMethod.Delete)
            method(HttpMethod.Patch)
            header(HttpHeaders.Authorization)
            header(HttpHeaders.AccessControlAllowOrigin)
            allowNonSimpleContentTypes = true
            allowCredentials = true
            allowSameOrigin = true
        }
        install(ContentNegotiation) {
            json()
        }
        routing {
            student()
            lesson()
        }
    }.start(wait = true)
}

val studentsList = listOf(
    Student("Sheldon", "Cooper"),
    Student("Leonard", "Hofstadter"),
    Student("Howard", "Wolowitz"),
    Student("Penny", "Hofstadter"),
).toMutableList()

val lessonsList = listOf(
    Lesson("Math", listOf(0, 1, 2).map { studentsList[it] }.toMutableList()),
    Lesson("Phys", listOf(0, 1).map { studentsList[it] }.toMutableList()),
    Lesson("Story", listOf(0, 1, 3).map { studentsList[it] }.toMutableList())
).toMutableList()

