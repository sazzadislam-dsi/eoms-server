package com.lynas.service

import com.lynas.model.Enrolment
import com.lynas.repo.EnrolmentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

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
    fun delete(enrolmentId: Long, studentId: Long, year: Int, orgId: Long): Boolean {
        val enrolment:Enrolment? = enrolmentRepository.findEnrollmentOfStudentByYear(studentId, year, orgId)
        return if (enrolment != null && enrolment.id == enrolmentId) {
            enrolmentRepository.delete(enrolmentId)
            true
        } else {
            false
        }
    }

    /**
     * This method check whether a student enrolled in a class or not in a given year. This method return
     * two values, the enrollment(this object can be NULL) and boolean value whether the student enrolled or not.
     *
     * @param studentId the entity id of the student
     * @param year      in which year student is going to enroll
     *
     * @return enrollment(can be null) object and boolean value (whether the student enrolled or not in the given year)
     */
    //data class Result(val enrolment: Enrolment?, val isEnroll: Boolean)

    @Transactional
    fun studentEnrolmentCheck(roleNumber: Int, studentId: Long, classId: Long, year: Int, orgId: Long): Pair<Boolean, String> {
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