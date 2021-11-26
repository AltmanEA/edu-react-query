package component

import kotlinext.js.jso
import kotlinx.html.INPUT
import kotlinx.html.js.onClickFunction
import react.Props
import react.dom.*
import react.fc
import react.query.useMutation
import react.query.useQuery
import react.query.useQueryClient
import react.router.dom.Link
import react.useRef
import ru.altmanea.eduReactQuery.model.Student
import serverUrl
import studentsPath
import wrappers.fetch

interface StudentListProps : Props {
    var students: List<Student>
    var addStudent: (String, String) -> Unit
}

fun fcStudentList() = fc("StudentList") { props: StudentListProps ->
    h3 { +"Students" }
    ol {
        props.students.map { student ->
            li {
                Link {
                    attrs.to = "/students/${student.idName}"
                    +student.fullName
                }
            }
        }
    }

    val firstnameRef = useRef<INPUT>()
    val surnameRef = useRef<INPUT>()

    form {
        p {
            +"Firstname: "
            input {
                ref = firstnameRef
            }
        }
        p {
            +"Surname: "
            input {
                ref = surnameRef
            }
        }
        button {
            +"Add student"
            attrs.onClickFunction = {
                firstnameRef.current?.value?.let { firstname ->
                    surnameRef.current?.value?.let { surname ->
                        props.addStudent(firstname, surname)
                    }
                }
            }
        }
    }
}

typealias QueryData = Array<Student>

fun qcStudentList() = fc("QueryStudentList") { _: Props ->
    val queryClient = useQueryClient()

    val query = useQuery<Any, Any, Any, Any>(
        "studentList",
        {
            fetch(serverUrl + studentsPath)
                .then { it.json() }
        }
    )

    val addStudentMutation = useMutation<Any, Any, Any, Any>(
        { _value: String ->
            fetch(
                serverUrl + studentsPath,
                jso {
                    method = "POST"
                    body = {
                        name = "name"
                    }
                }
            )
        },
//        options = jso {
//            onSuccess = { _: Any, _: Any, _: Any? ->
//                queryClient.invalidateQueries("studentList")
//            }
//        }
    )

    if (query.isLoading) div { +"Loading .." }
    else if (query.isError) div { +"Error!" }
    else {
        val data = query.data.unsafeCast<QueryData>()
        child(fcStudentList()) {
            attrs.students = data.map { Student(it.firstname, it.surname) }
            attrs.addStudent = {f, s ->
                console.log(Student(f, s).idName)
                addStudentMutation.mutate
            }
        }
    }
}

