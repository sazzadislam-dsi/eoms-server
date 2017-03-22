package com.lynas.model.request

import com.lynas.model.util.ExamType

data class ExamJson (
    var examType: ExamType? = null,
    var totalNumber: Long? = null,
    var obtainedNumber: Long? = null


)