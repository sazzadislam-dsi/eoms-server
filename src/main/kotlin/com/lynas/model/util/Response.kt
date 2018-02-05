package com.lynas.model.util

import com.lynas.model.FeeInfo
import java.util.*

data class Subject(
        var id: Long? = null,
        var subjectName: String,
        var subjectDescription: String,
        var subjectBookAuthor: String
)

data class FeeInfoOfStudentResponse(
        val id: Long,
        val paymentDate: Date,
        val studentId: Long,
        var feeInfo: FeeInfo
)

data class EnrolmentDelete(
        val enrolmentId: Long,
        val studentId: Long,
        val year: Int,
        val roleNumber: Int,
        val isDeleted: Boolean
)
