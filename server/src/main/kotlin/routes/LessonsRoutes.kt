package routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import lessonsList
import lessonsPath
import model.Lesson
import studentsList


fun Route.lesson() =
    route(lessonsPath) {
        get {
            if (lessonsList.isNotEmpty()) {
                call.respond(lessonsList)
            } else {
                call.respondText("No lessons found", status = HttpStatusCode.NotFound)
            }
        }
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            val lesson =
                lessonsList.find { it.name == id } ?: return@get call.respondText(
                    "No lesson with name $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(lesson)
        }
        post {
            val lesson = call.receive<Lesson>()
            lessonsList.add(lesson)
            call.respondText("Lesson stored correctly", status = HttpStatusCode.Created)
        }
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (lessonsList.removeIf { it.name == id }) {
                call.respondText("Lesson removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }

        post("{lessonId}/students/{studentId}") {
            val lessonId = call.parameters["lessonId"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val studentId = call.parameters["studentId"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val lesson = lessonsList.find { it.name == lessonId } ?: return@post call.respondText(
                "No lesson with name $lessonId",
                status = HttpStatusCode.NotFound
            )
            val student = studentsList.find { it.idName == studentId } ?: return@post call.respondText(
                "No student with full name $studentId",
                status = HttpStatusCode.NotFound
            )
            lesson.students += student
            call.respond(lesson)
        }
        delete("{lessonId}/students/{studentId}") {
            val lessonId = call.parameters["lessonId"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val studentId = call.parameters["studentId"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val lesson = lessonsList.find { it.name == lessonId } ?: return@delete call.respondText(
                "No lesson with name $lessonId",
                status = HttpStatusCode.NotFound
            )
            val student = studentsList.find { it.idName == studentId } ?: return@delete call.respondText(
                "No student with full name $studentId",
                status = HttpStatusCode.NotFound
            )
            lesson.students -= student
            call.respond(lesson)
        }

        post("{lessonId}/students/{studentId}/marks") {
            val lessonId = call.parameters["lessonId"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val studentId = call.parameters["studentId"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val mark = call.receive<String>()
            val lesson = lessonsList.find { it.name == lessonId } ?: return@post call.respondText(
                "No lesson with name $lessonId",
                status = HttpStatusCode.NotFound
            )
            val student = studentsList.find { it.idName == studentId } ?: return@post call.respondText(
                "No student with full name $studentId",
                status = HttpStatusCode.NotFound
            )
            if(student !in lesson.students)
                call.respondText(
                    "No student $studentId in lesson $lessonId",
                    status = HttpStatusCode.NotFound
                )
            lesson.marks += studentId to mark
            call.respond(lesson)
        }
        delete("{lessonId}/students/{studentId}/marks") {
            val lessonId = call.parameters["lessonId"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val studentId = call.parameters["studentId"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val lesson = lessonsList.find { it.name == lessonId } ?: return@delete call.respondText(
                "No lesson with name $lessonId",
                status = HttpStatusCode.NotFound
            )
            val student = studentsList.find { it.idName == studentId } ?: return@delete call.respondText(
                "No student with full name $studentId",
                status = HttpStatusCode.NotFound
            )
            if(student !in lesson.students)
                call.respondText(
                    "No student $studentId in lesson $lessonId",
                    status = HttpStatusCode.NotFound
                )
            val mark = lesson.marks.find { it.first == studentId }
            if(mark==null){
                call.respondText(
                    "Student $studentId has not mark in lesson $lessonId",
                    status = HttpStatusCode.NotFound
                )
            }
            lesson.marks.remove(mark)
            call.respond(lesson)
        }
    }


/* curl for debug
curl http://127.0.0.1:8080/lessons
curl http://127.0.0.1:8080/lessons/Math
curl -d {"name":"Chem"} -H "Content-Type: application/json"  http://127.0.0.1:8080/lessons
curl -X DELETE http://127.0.0.1:8080/lessons/Story

curl -X POST http://127.0.0.1:8080/lessons/Math/students/PennyHofstadter
curl -X DELETE http://127.0.0.1:8080/lessons/Math/students/SheldonCooper

curl -d 5 -H "Content-Type: text/plain"  http://127.0.0.1:8080/lessons/Math/students/SheldonCooper/marks
curl -d 5 -H "Content-Type: text/plain"  http://127.0.0.1:8080/lessons/Math/students/PennyHofstadter/marks
curl -X DELETE http://127.0.0.1:8080/lessons/Math/students/SheldonCooper/marks
curl -X DELETE http://127.0.0.1:8080/lessons/Math/students/PennyHofstadter/marks
*/