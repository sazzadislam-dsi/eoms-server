package com.lynas.service

import com.lynas.dto.StudentInfoQueryResult
import com.lynas.exception.EntityNotFoundException
import com.lynas.model.Student
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
        return studentRepository.findOne(id, orgId) ?: throw EntityNotFoundException("Student not found with id: $id")
    }

    @Transactional
    fun searchByFirstName(name: String, orgId: Long): List<Student> {
        return studentRepository.searchByFirstName(name, orgId)
    }

    @Transactional
    fun findAll(orgId: Long): List<Student> {
        return studentRepository.findAll(orgId).toList()
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