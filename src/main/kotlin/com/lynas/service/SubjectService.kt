package com.lynas.service

import com.lynas.model.Subject
import com.lynas.repo.SubjectRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by seal on 2/6/2017.
 */
@Service
open class SubjectService constructor(val subjectRepository: SubjectRepository) {

    @Transactional
    open fun post(subject: Subject) {
        subjectRepository.save(subject)
    }

    @Transactional
    open fun findById(subjectId: Long): Subject {
        return subjectRepository.findOne(subjectId)
    }

    @Transactional
    open fun findAllByClassId(classId: Long): List<Subject> {
        return subjectRepository.findAllByClassId(classId)
    }

    @Transactional
    open fun findAllByStudentId(stdId: Long): List<Subject> {
        return subjectRepository.findAllByStudentId(stdId)

    }

}