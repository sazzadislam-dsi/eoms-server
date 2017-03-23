package com.lynas.service

import com.lynas.model.Exam
import com.lynas.repo.ExamRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by sazzad on 7/19/16
 */

@Service
class ExamService(val examRepository: ExamRepository) {

    @Transactional
    fun save(exam: Exam) {
        examRepository.save(exam)
    }
}