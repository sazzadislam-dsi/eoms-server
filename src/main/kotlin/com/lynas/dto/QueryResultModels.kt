package com.lynas.dto

import com.lynas.model.Exam
import com.lynas.model.util.ExamType
import org.springframework.data.neo4j.annotation.QueryResult
import java.util.*

/**
 * Created by seal on 4/11/2017.
 */
@QueryResult
class AttendanceViewQueryResult : ClassDetailQueryResult()  {
    var isPresent: Boolean? = null
}

@QueryResult
open class ClassDetailQueryResult {
    var studentId: Long? = null
    var studentName: String? = null
    var roleNumber: Int? = null
    var enrolmentId: Long? = null

}

@QueryResult
open class ExamListQueryResult {
    var date: Date = Date()
    var examType: ExamType = ExamType.FINAL
}

@QueryResult
data class ExamQueryResult (
        var courseName: String? = null,
        var exam: List<Exam> = mutableListOf(),
        var subject: String? = null,
        var person: String? = null,
        var roleNumber: Int? = null,
        var studentId: Long? = null
)

@QueryResult
data class ResultUpdateQueryResult (
        var courseName: String? = null,
        var exam: List<Exam> = mutableListOf(),
        var subject: String? = null,
        var person: String? = null,
        var roleNumber: Int? = null,
        var studentId: Long? = null,
        var percentile: Double? = null,
        var totalNumber: Double? = null
)


@QueryResult
data class StudentInfoQueryResult(
        var studentId: Long = 0,
        var classId: Long = 0,
        var firstName: String = "",
        var lastName: String = "",
        var className: String = "",
        var rollNumber: Int = 0,
        var year: Int = 0
)

