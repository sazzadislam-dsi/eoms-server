package com.lynas.dto

import com.lynas.model.ContactInformation
import com.lynas.model.Course
import com.lynas.model.Organization
import com.lynas.model.OrganizationInfo
import com.lynas.model.util.*

data class AttendanceDTO(
        val isPresent: Boolean,
        val studentId: Long
)


data class AttendanceOfClassDTO(
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
        var mark: Double,
        var studentId: Long,
        var isPresent: Boolean
)

data class ExamJsonWrapper(
        var classId: Long,
        var subjectId: Long,
        var totalMark: Double,
        var percentile: Double,
        var date: String,
        var year: Int,
        var examType: ExamType,
        var examDTO: Array<ExamDTO>)

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
        var section: Section){
    fun getCourse(organization: Organization):Course{
        return Course(name=name, shift = shift, section = section,organization = organization)
    }
}


data class OrganizationDTO(
        var id: Long? = null,
        var name:String,
        var establishmentYear:Int,
        var organizationInfo: OrganizationInfoDTO
){
    fun toOrganization() = Organization(
            name=this.name,
            establishmentYear = this.establishmentYear,
            organizationInfo = OrganizationInfo(
                    founderName = this.organizationInfo.founderName,
                    founderDescription = this.organizationInfo.founderDescription))
}

data class OrganizationInfoDTO(
        var id: Long? = null,
        var founderName: String,
        var founderDescription: String
)
