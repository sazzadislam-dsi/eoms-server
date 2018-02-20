package com.lynas.service

import com.lynas.dto.EnrolmentDeleteDTO
import com.lynas.model.Enrolment
import com.lynas.repo.EnrolmentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by sazzad on 7/19/16
 */

@Service
class EnrolmentService(val enrolmentRepository: EnrolmentRepository) {

    @Transactional
    fun create(enrolment: Enrolment) {
        enrolmentRepository.save(enrolment)
    }

    @Transactional
    fun delete(enrolmentId: Long, studentId: Long, year: Int, orgId: Long): EnrolmentDeleteDTO {
        val enrolment:Enrolment? = enrolmentRepository.findEnrollmentOfStudentByYear(studentId, year, orgId)
        return if (enrolment != null && enrolment.id == enrolmentId) {
            enrolmentRepository.delete(enrolmentId)
            return EnrolmentDeleteDTO(enrolmentId, studentId, year, enrolment.roleNumber, true)
        } else {
            return EnrolmentDeleteDTO(enrolmentId, studentId, year, enrolment?.roleNumber!!, false)
        }
    }

    @Transactional
    fun studentEnrolmentCheck(roleNumber: Int, studentId: Long, classId: Long, year: Int, orgId: Long)
            : Pair<Boolean, String> {
        val enrolment = enrolmentRepository.findEnrollmentOfStudentByYear(studentId, year, orgId)
        return if (enrolment == null) {
            val enrolmentList = enrolmentRepository.findEnrollmentOfRole(roleNumber, year, orgId, classId)
            if (enrolmentList.isNotEmpty()) {
                false to "Role already exist"
            } else {
                true to ""
            }
        } else {
            false to "Enrolment already exist"
        }
    }

}