package com.lynas.service

import com.lynas.model.Student
import com.lynas.model.query.result.StudentInfoQueryResult
import com.lynas.repo.StudentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by sazzad on 7/19/16
 */

@Service
open class StudentService(val studentRepository: StudentRepository) {

    @Transactional
    open fun save(student: Student): Student {
        return studentRepository.save(student)
    }

    @Transactional
    open fun findById(id: Long, orgId: Long): Student {
        return studentRepository.findOne(id, orgId)
    }

    @Transactional
    open fun searchByFirstName(name: String, orgId: Long) : List<Student> {
        return studentRepository.searchByFirstName(name, orgId)
    }

    @Transactional
    open fun findStudentByContactId(id: Long, orgId : Long): Student {
        return studentRepository.findStudentByContactId(id, orgId)
    }

    @Transactional
    open fun findAll(orgId : Long): MutableList<Student> {
        var studentList: MutableList<Student> = mutableListOf()
        studentRepository.findAll(orgId).forEach { i -> studentList.add(i) }
        return studentList
    }

    @Transactional
    open fun studentInfoByYear(id: Long, year: Int, orgId : Long): StudentInfoQueryResult {
        return studentRepository.studentInfoByYear(id, year, orgId)
    }

}