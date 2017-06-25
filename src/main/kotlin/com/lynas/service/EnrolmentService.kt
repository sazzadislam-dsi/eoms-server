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
open class EnrolmentService (val enrolmentRepository: EnrolmentRepository) {

    @Transactional
    open fun save(enrolment: Enrolment) {
        enrolmentRepository.save(enrolment)
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
    data class Result(val enrolment: Enrolment?, val isEnroll: Boolean)
    @Transactional
    open fun studentEnrolmentCheck(studentId: Long, year: Int, orgId: Long): Result {
        val enrolment: Enrolment? = enrolmentRepository.findEnrollmentOfStudentByYear(studentId, year, orgId)
        return Result(enrolment, Objects.nonNull(enrolment))
    }
}