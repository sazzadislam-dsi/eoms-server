package com.lynas.dto

import com.lynas.model.FeeInfo
import java.util.*

data class SubjectResponseDTO(
        var id: Long? = null,
        var subjectName: String,
        var subjectDescription: String,
        var subjectBookAuthor: String
)

data class StudentFeeResponseDTO(
        val id: Long,
        val paymentDate: Date,
        val studentId: Long,
        var feeInfo: FeeInfo
)

data class EnrolmentDeleteDTO(
        val enrolmentId: Long,
        val studentId: Long,
        val year: Int,
        val roleNumber: Int,
        val isDeleted: Boolean
)
