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
import styled.styledButton
import wrappers.AxiosResponse
import wrappers.QueryError
import wrappers.axios
import kotlin.js.json

//import wrappers.fetch

interface StudentListProps : Props {
    var students: List<Student>
    var addStudent: (String, String) -> Unit
    var deleteStudent: (Int) -> Unit
}

fun fcStudentList() = fc("StudentList") { props: StudentListProps ->
    h3 { +"Students" }
    ol {
        props.students.mapIndexed { index, student ->
            li {
                +"${student.fullName} \t"
                button {
                    +"X"
                    attrs.onClickFunction = {
                        props.deleteStudent(index)
                    }
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

    val query = useQuery<Any, QueryError, AxiosResponse<QueryData>, Any>(
        "studentList",
        {
            axios<QueryData>(jso {
                url = serverUrl + studentsPath
            })
        }
    )

    val addStudentMutation = useMutation<Any, Any, Any, Any>(
        { student: Student ->
            axios<String>(jso {
                url = serverUrl + studentsPath
                method = "Post"
                headers = json(
                    "Content-Type" to "application/json"
                )
                data = JSON.stringify(student)
            })
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>("studentList")
            }
        }
    )

    val deleteStudentMutation = useMutation<Any, Any, Any, Any>(
        { student: Student ->
            axios<String>(jso {
                url = "$serverUrl$studentsPath/${student.idName}"
                method = "Delete"
            })
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>("studentList")
            }
        }
    )


    if (query.isLoading) div { +"Loading .." }
    else if (query.isError) div { +"Error!" }
    else {
        val data = query.data?.data.unsafeCast<QueryData>()
        val students = data.map { Student(it.firstname, it.surname) }
        child(fcStudentList()) {
            attrs.students = students
            attrs.addStudent = { f, s ->
                addStudentMutation.mutate(Student(f, s), null)
            }
            attrs.deleteStudent = {
                deleteStudentMutation.mutate(students[it], null)
            }
        }
    }
}

