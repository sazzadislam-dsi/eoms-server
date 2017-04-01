package com.lynas.model.response

import com.lynas.model.util.ExamType

/**
 * Created by seal on 3/18/2017.
 */
class ExamStudentResponse {
    var studentName: String? = ""
    var className: String? = ""
    var rollNumber: Int? = 0
    var year: Int? = 0

    var examBySubject: Map<String?, List<Exam>> = mutableMapOf()
    var resultBySubject: Map<String?, Double> = mutableMapOf()

    class Exam {
        var examType: ExamType? = null
        var totalMark: Double? = 0.0
        var obtainMark: Double? = 0.0
    }
}