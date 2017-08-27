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
class StudentService(val studentRepository: StudentRepository) {

    @Transactional
    fun create(student: Student): Student {
        return studentRepository.save(student)
    }

    @Transactional
    fun findById(id: Long, orgId: Long): Student {
        return studentRepository.findOne(id, orgId)
    }

    @Transactional
    fun searchByFirstName(name: String, orgId: Long): List<Student> {
        return studentRepository.searchByFirstName(name, orgId)
    }

    @Transactional
    fun findStudentByContactId(id: Long, orgId: Long): Student {
        return studentRepository.findStudentByContactId(id, orgId)
    }

    @Transactional
    fun findAll(orgId: Long): MutableList<Student> {
        val studentList: MutableList<Student> = mutableListOf()
        studentRepository.findAll(orgId).forEach { i -> studentList.add(i) }
        return studentList
    }

    @Transactional
    fun studentInfoByYear(id: Long, year: Int, orgId: Long): StudentInfoQueryResult {
        return studentRepository.studentInfoByYear(id, year, orgId)
    }

    @Transactional
    fun findStudentCountOfOrganization(orgId: Long): Int {
        return studentRepository.studentCountOfAnOrganization(orgId)
    }

}