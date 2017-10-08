package com.lynas.service.dto

import com.lynas.model.Exam
import com.lynas.model.util.ExamType
import java.util.*

class ExamOfSubjectUpdateDTO {
    var classId: Long = -1L
    var subjectId: Long = -1L
    var examType: ExamType = ExamType.NULL
    var date: Date = Date()
    var resultOfASubjectByExamTypeDate: MutableList<ExamOfStudent> = mutableListOf()
    var percentile: Double = 0.0
    var totalNumber: Double = 0.0
}

data class ExamOfStudent(val studentId: Long, val name: String, val rollNumber: Int, val examId: Long,
                         val obtainMark: Double, val p: Boolean)