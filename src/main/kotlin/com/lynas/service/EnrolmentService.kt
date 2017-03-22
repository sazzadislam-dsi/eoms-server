package com.lynas.service

import com.lynas.model.Enrolment
import com.lynas.repo.EnrolmentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by sazzad on 7/19/16
 */

@Service
open class EnrolmentService (val enrolmentRepository: EnrolmentRepository) {

    @Transactional
    open fun save(enrolment: Enrolment) {
        enrolmentRepository.save(enrolment)
    }

    @Transactional
    open fun studentEnrolmentCheck(studentId: Long): Boolean {
        return enrolmentRepository.countStudentEnrolment(studentId) == 0
    }
}