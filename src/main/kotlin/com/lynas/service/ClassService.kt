package com.lynas.service

import com.lynas.model.Course
import com.lynas.model.query.result.ClassDetailQueryResult
import com.lynas.repo.ClassRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by sazzad on 7/19/16
 */

@Service
open class ClassService(val classRepo: ClassRepository) {

    @Transactional
    open fun save(course: Course): Course {
        return classRepo.save(course)
    }

    @Transactional
    open fun findListByOrganizationName(name: String?): List<Course> {
        return classRepo.findListByOrganizationName(name)
    }

    data class CourseInfo(var id: Long?, var name: String?)

    open fun findClassListByOrganizationName(name: String?): List<CourseInfo> {
        return findListByOrganizationName(name).map {
            CourseInfo(id = it.id,
                    name = it.name + " " + it.section + " " + it.shift
            )
        }
    }

    @Transactional
    open fun findById(id: Long, organization: String): Course {
        return classRepo.findById(id, organization)
    }


    @Transactional
    open fun deleteById(id: Long) {
        return classRepo.delete(id)
    }

    @Transactional
    open fun findStudentsByClassId(classID: Long, organization: String, year: Int): Collection<ClassDetailQueryResult> {
        return classRepo.findStudentsByClass(classID, organization, year)
    }

    @Transactional
    open fun findStudentsByClassId(classID: Long, year: Int, organization: String): Collection<ClassDetailQueryResult> {
        return classRepo.findStudentsByClass(classID, year, organization)
    }

    @Transactional
    fun findListCountByOrganizationName(name: String?): Int {
        return classRepo.findListCountByOrganizationName(name)
    }


    fun checkClassAlreadyExist(course: Course, orgId: Long): Boolean {
        val cc = classRepo.findByPropAndOrg(
                course.name!!,
                course.shift.toString(),
                course.section.toString(),
                orgId)
        return cc != null
    }
}