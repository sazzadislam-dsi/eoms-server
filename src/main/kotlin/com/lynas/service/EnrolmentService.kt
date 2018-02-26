package com.lynas.service

import com.lynas.dto.EnrolmentDTO
import com.lynas.dto.EnrolmentDeleteDTO
import com.lynas.exception.DuplicateEntryException
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
        val enrolment: Enrolment? = enrolmentRepository.findEnrollmentOfStudentByYear(studentId, year, orgId)
        return if (enrolment != null && enrolment.id == enrolmentId) {
            enrolmentRepository.delete(enrolmentId)
            return EnrolmentDeleteDTO(enrolmentId, studentId, year, enrolment.roleNumber, true)
        } else {
            return EnrolmentDeleteDTO(enrolmentId, studentId, year, enrolment?.roleNumber!!, false)
        }
    }

    @Transactional
    fun validateStudentEnrolment(enrolmentDTO: EnrolmentDTO, orgId: Long) {
        enrolmentRepository.findEnrollmentOfStudentByYear(enrolmentDTO.studentId, enrolmentDTO.year, orgId)
                ?: throw DuplicateEntryException("Enrolment already exist")
        if (enrolmentRepository.findEnrollmentOfRole(
                        enrolmentDTO.roleNumber, enrolmentDTO.year, orgId, enrolmentDTO.classId).isNotEmpty()) {
            throw DuplicateEntryException("Role already exist")
        }
    }

}