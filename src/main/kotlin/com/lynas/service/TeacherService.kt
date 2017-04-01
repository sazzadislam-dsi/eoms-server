package com.lynas.service

import com.lynas.model.Teacher
import com.lynas.repo.PersonRepository
import com.lynas.repo.TeacherRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by sazzad on 7/19/16
 */

@Service
open class TeacherService(
        val teacherRepository: TeacherRepository,
        val personRepository: PersonRepository) {

    @Transactional
    open fun save(teacher: Teacher) {
        teacher.person = personRepository.save(teacher.person)
        teacherRepository.save(teacher)
    }
}