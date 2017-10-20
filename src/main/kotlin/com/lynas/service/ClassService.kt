package com.lynas.service

import com.lynas.exception.DuplicateCourseException
import com.lynas.model.Course
import com.lynas.model.util.ClassDetailQueryResult
import com.lynas.repo.ClassRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by sazzad on 7/19/16
 */

@Service
class ClassService(val classRepo: ClassRepository) {

    @Transactional
    fun create(course: Course): Course {
        val foundDuplicate = classRepo.findListByOrganizationId(course.organization!!.id!!)
                .filter {
                    it.name == course.name &&
                    it.section == course.section &&
                    it.shift == course.shift
                }
                .isEmpty()
                .not()
        if (foundDuplicate) {
            throw DuplicateCourseException("Duplicate Class Found")
        }
        return classRepo.save(course)
    }

    @Transactional
    fun findListByOrganizationId(orgId: Long): List<Course> {
        return classRepo.findListByOrganizationId(orgId)
    }

    data class CourseInfo (var id: Long?, var name: String?)

    fun findClassListByOrganizationId(orgId: Long): List<CourseInfo> {
        return findListByOrganizationId(orgId).map {
            CourseInfo( id = it.id,
                    name = it.name + " " + it.section + " " + it.shift
            )
        }
    }

    @Transactional
    fun findById(id: Long, orgId: Long): Course? {
        return classRepo.findById(id, orgId)
    }


    @Transactional
    fun deleteById(id: Long) {
        return classRepo.delete(id)
    }

    @Transactional
    fun findStudentsByClassId(classID: Long, orgId: Long, year: Int): Collection<ClassDetailQueryResult> {
        return classRepo.findStudentsByClass(classID, orgId, year)
    }

    @Transactional
    fun findStudentsByClassId(classID: Long, year: Int, orgId: Long): Collection<ClassDetailQueryResult> {
        return classRepo.findStudentsByClass(classID, year, orgId)
    }

    @Transactional
    fun findListCountByOrganizationName(orgId: Long): Int {
        return classRepo.findListCountByOrganizationName(orgId)
    }

    fun checkClassAlreadyExist(course: Course, orgId: Long): Boolean {
        val cc = classRepo.findByPropAndOrg(
                course.name,
                course.shift.toString(),
                course.section.toString(),
                orgId)
        return cc != null
    }
}