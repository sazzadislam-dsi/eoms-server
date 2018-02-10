package com.lynas.dto

import com.lynas.model.ContactInformation
import com.lynas.model.util.*

// TODO rename variable name
data class AttendanceDTO(
        var i: Boolean = false,
        var t: Long = 0
)


data class AttendanceDTOofClass(
    val classId: Long,
    val date: String,
    val attendanceDTO: Array<AttendanceDTO>
)


data class EnrolmentDTO(
        var studentId: Long = 1,
        var classId: Long = 1,
        var year: Int = 1,
        var roleNumber: Int = 1
)

data class ExamDTO(
        val mark: Double,
        val studentId: Long,
        val isPresent: Boolean
)

data class ExamJsonWrapper(
        val classId: Long,
        val subjectId: Long,
        val totalMark: Double,
        val percentile: Double,
        val date: String,
        val year: Int,
        val examType: ExamType,
        val examDTO: Array<ExamDTO>)

data class ExamUpdateDTO(val examId: Long, val updateObtainMark: Double)

data class FeeInfoDTO(
        val classId: Long = 99999,
        val type: String = "",
        val amount: Double = 0.0,
        val year: Int = 2017,
        val lastDate: String? = null)

data class PayFeeDTO(
        val classId: Long = 9999,
        val studentId: Long = 9999,
        val feeInfoId: Long = 9999,
        val paymentDate: String = ""
)

data class PersonContactDTO(
        var personId: Long,
        var contactInformation: ContactInformation)


data class StudentContactDTO(
        val studentId: Long = 1,
        val name: String = "",
        val address: String = "",
        val phone_1: String = "",
        val phone_2: String = "",
        val phone_3: String = "",
        val contactType: ContactType = ContactType.PRIMARY

)

data class StudentDTO(
        var studentId: Long = 1,
        var firstName: String = "",
        var lastName: String = "",
        var dateOfBirth: String = "",
        var sex: Sex = Sex.MALE,
        var religion: Religion = Religion.MUSLIM
)


data class SubjectDTO(
        var subjectName: String,
        var subjectDescription: String,
        var subjectBookAuthor: String,
        var classId: Long
)


data class CourseDTO(
        var name: String,
        var shift: Shift,
        var section: Section
)