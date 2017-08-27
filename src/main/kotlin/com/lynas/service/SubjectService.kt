package com.lynas.service

import com.lynas.model.Subject
import com.lynas.repo.SubjectRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by seal on 2/6/2017.
 */
@Service
class SubjectService constructor(val subjectRepository: SubjectRepository) {

    @Transactional
    fun create(subject: Subject) {
        subjectRepository.save(subject)
    }

    @Transactional
    fun findById(subjectId: Long): Subject? {
        return subjectRepository.findOne(subjectId)
    }

    @Transactional
    fun findAllByClassId(classId: Long, orgId: Long): List<Subject> {
        return subjectRepository.findAllByClassId(classId, orgId)
    }

    @Transactional
    fun findAllByStudentId(stdId: Long, orgId: Long): List<Subject> {
        return subjectRepository.findAllByStudentId(stdId, orgId)
    }

}