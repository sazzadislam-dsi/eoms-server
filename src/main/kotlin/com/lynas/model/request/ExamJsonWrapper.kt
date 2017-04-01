package com.lynas.model.request

import com.lynas.model.util.ExamType


/**
 * Created by seal on 2/17/2017.
 */
class ExamJsonWrapper {
    val classId = 0L
    val subjectId  = 0L
    val totalMark = 0.0
    val percentile = 0.0
    val year = 0
    val examType: ExamType = ExamType.FINAL
    val examJson: Array<ExamJson> = arrayOf()
}
