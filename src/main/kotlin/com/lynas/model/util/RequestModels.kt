package com.lynas.model.util

import com.lynas.model.ContactInformation


data class AttendanceJson(
        var i: Boolean = false,
        var t: Long = 0
)


class AttendanceJsonOfDay {
    val classId: Long = 0
    val date: String = ""
}

class AttendanceJsonWrapper {
    val classId: Long = 0
    val date: String = ""
    val attendanceJson: Array<AttendanceJson> = arrayOf()
}

data class EnrolmentJson(
        var studentId: Long = 1,
        var classId: Long = 1,
        var year: Int = 1,
        var roleNumber: Int = 1
)

data class ExamJson(
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
        val examJson: Array<ExamJson>)

data class ExamUpdateJson(val examId: Long, val updateObtainMark: Double)

data class FeeInfoJson(
        val classId: Long = 99999,
        val type: String = "",
        val amount: Double = 0.0,
        val year: Int = 2017,
        val lastDate: String? = null)

data class FeeStudentNew(
        val classId: Long = 9999,
        val studentId: Long = 9999,
        val feeInfoId: Long = 9999,
        val paymentDate: String = ""
)

class PersonContact {
    var personId: Long = 0
    var contactInformation: ContactInformation = ContactInformation()
}


data class StudentContact(
        val studentId: Long = 1,
        val name: String = "",
        val address: String = "",
        val phone_1: String = "",
        val phone_2: String = "",
        val phone_3: String = "",
        val contactType: ContactType = ContactType.PRIMARY

)

data class StudentJson(
        var studentId: Long = 1,
        var firstName: String = "",
        var lastName: String = "",
        var dateOfBirth: String = "",
        var sex: Sex = Sex.MALE,
        var religion: Religion = Religion.MUSLIM
)


data class SubjectPostJson(

        var subjectName: String? = null,
        var subjectDescription: String? = null,
        var subjectBookAuthor: String? = null,
        var classId: Long? = null
)


class CourseJson(
        var name: String,
        var shift: Shift,
        var section: Section
)