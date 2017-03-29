package com.lynas.service

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
    fun save(student: Student): Student {
        return studentRepository.save(student)
    }

    @Transactional
    fun findById(id: Long): Student {
        return studentRepository.findOne(id)
    }

    @Transactional
    fun searchByFirstName(name: String): List<Student> {
        return studentRepository.searchByFirstName(name)
    }

    @Transactional
    fun findStudentByContactId(id: Long): Student {
        return studentRepository.findStudentByContactId(id)
    }

    @Transactional
    fun findAll(): MutableList<Student> {
        var studentList: MutableList<Student> = mutableListOf()
        studentRepository.findAll().forEach { i -> studentList.add(i) }
        return studentList
    }

    @Transactional
    fun getTotalNumberOfStudents(): Int {
        return studentRepository.getTotalNumberOfStudents()
    }

}