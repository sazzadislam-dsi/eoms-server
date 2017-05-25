package com.lynas.model.response

import com.lynas.model.Exam

/**
 * Created by seal on 5/26/2017.
 */
class ExamClassResponse1 {
    var studentId = 0L
    var classId = 0L
    var rollNumber = 0
    var year = 0

    data class GrandTotal(
            var total: Double = 0.0,
            var obtained: Double = 0.0
    )

    var grandTotal: GrandTotal = GrandTotal()

    data class Subjects (
            var subjectName: String = "N/A",
            var total: Double = 0.0,
            var obtained: Double = 0.0,
            var exams: List<Exam> = listOf()
    )

    var subjects: List<Subjects> = mutableListOf()
}