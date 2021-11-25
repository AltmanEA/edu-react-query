package routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.altmanea.eduReactQuery.model.Student
import ru.altmanea.eduReactQuery.studentsList
import ru.altmanea.eduReactQuery.studentsPath


fun Route.student() =
    route(studentsPath) {
        get {
            if (studentsList.isNotEmpty()) {
                call.respond(studentsList)
            } else {
                call.respondText("No students found", status = HttpStatusCode.NotFound)
            }
        }
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            val student =
                studentsList.find { it.idName == id } ?: return@get call.respondText(
                    "No student with full name $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(student)
        }
        post {
            val student = call.receive<Student>()
            studentsList.add(student)
            call.respondText("Student stored correctly", status = HttpStatusCode.Created)
        }
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (studentsList.removeIf { it.idName == id }) {
                call.respondText("Lesson removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }


/* curl for debug
curl http://127.0.0.1:8080/students
curl http://127.0.0.1:8080/students/SheldonCooper
curl -d {"firstname":"Penny","surname":"Hofstadter"} -H "Content-Type: application/json"  http://127.0.0.1:8080/students
curl -X DELETE http://127.0.0.1:8080/students/HowardWolowitz
*/