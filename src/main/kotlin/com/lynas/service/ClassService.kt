package com.lynas.service

import com.lynas.model.Course
import com.lynas.model.query.result.ClassDetailQueryResult
import com.lynas.repo.ClassRepository
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by sazzad on 7/19/16
 */

@Service
open class ClassService(val classRepo: ClassRepository) {

    @Transactional
    open fun save(course: Course): Course {
        val foundCourse = classRepo.findByProperty(course.name, course.shift.toString(), course.section.toString(), course.organization?.name)
        if (null == foundCourse) {
            return classRepo.save(course)
        } else {
            throw DuplicateKeyException(course.toString())
        }
    }

    @Transactional
    open fun findListByOrganizationName(name: String?): List<Course> {
        return classRepo.findListByOrganizationName(name)
    }

    @Transactional
    open fun findById(id: Long): Course {
        return classRepo.findById(id)
    }


    @Transactional
    open fun deleteById(id: Long) {
        return classRepo.delete(id)
    }

    @Transactional
    open fun findStudentsByClassId(classID: Long): Collection<ClassDetailQueryResult> {
        return classRepo.findStudentsByClass(classID)
    }
}