package com.lynas.model.response

import com.lynas.model.Exam
import com.lynas.model.util.ExamType

/**
 * Created by seal on 5/26/2017.
 */
class ExamClassResponse1 {
    var studentId: Long? = 0L
    var classId: Long? = 0L
    var rollNumber: Int? = 0
    var year: Int? = 0

    constructor(studentId: Long, classId: Long, rollNumber: Int, year: Int, grandTotal: GrandTotal, subjects: List<Subjects>) {
        this.studentId = studentId
        this.classId = classId
        this.rollNumber = rollNumber
        this.year = year
        this.grandTotal = grandTotal
        this.subjects = subjects
    }

    data class GrandTotal(
            var total: Double? = 0.0,
            var obtained: Double? = 0.0
    )

    var grandTotal: GrandTotal = GrandTotal()

    data class Subjects (
            var subjectName: String? = "N/A",
            var total: Double? = 0.0,
            var obtained: Double? = 0.0,
            var exams: Map<ExamType, Exam>? = mutableMapOf()
    )

    var subjects: List<Subjects> = mutableListOf()
}