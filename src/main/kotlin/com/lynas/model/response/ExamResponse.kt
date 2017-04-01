package com.lynas.model.response

import com.lynas.model.util.ExamType

/**
 * Created by seal on 2/26/2017.
 */
class ExamResponse {
    var className: String? = ""
    var year: Int? = 0
    var subjectName: String? = ""
    var student = mutableListOf<Student>()

    class Student {
        var name: String? = ""
        var rollNumber: Int? = 0
        var exams = mutableListOf<Exam>()
        var result: Double? = 0.0
    }

    class Exam {
        var examType: ExamType? = null
        var totalMark: Double? = 0.0
        var obtainMark: Double? = 0.0
    }
}