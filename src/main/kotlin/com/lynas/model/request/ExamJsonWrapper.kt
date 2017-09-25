package com.lynas.model.request

import com.lynas.model.util.ExamType


/**
 * Created by seal on 2/17/2017.
 */
data class ExamJsonWrapper(
        val classId: Long,
        val subjectId: Long,
        val totalMark: Double,
        val percentile: Double,
        val date: String,
        val year: Int,
        val examType: ExamType,
        val examJson: Array<ExamJson>)

