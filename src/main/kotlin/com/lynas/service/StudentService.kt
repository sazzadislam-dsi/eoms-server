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
    open fun findById(id: Long, organization: String): Student {
        return studentRepository.findOne(id, organization)
    }

    @Transactional
    open fun searchByFirstName(name: String, organization: String) : List<Student> {
        return studentRepository.searchByFirstName(name, organization)
    }

    @Transactional
    open fun findStudentByContactId(id: Long, organization: String): Student {
        return studentRepository.findStudentByContactId(id, organization)
    }

    @Transactional
    open fun findAll(organization: String): MutableList<Student> {
        var studentList: MutableList<Student> = mutableListOf()
        studentRepository.findAll(organization).forEach { i -> studentList.add(i) }
        return studentList
    }

    @Transactional
    open fun studentInfoByYear(id: Long, year: Int, organization: String): StudentInfoQueryResult {
        return studentRepository.studentInfoByYear(id, year, organization)
    }

}