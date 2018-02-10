package com.lynas.controller

import com.lynas.dto.EnrolmentDTO
import com.lynas.exception.DuplicateEntryException
import com.lynas.exception.EntityNotFoundException
import com.lynas.model.Enrolment
import com.lynas.service.ClassService
import com.lynas.service.EnrolmentService
import com.lynas.service.StudentService
import com.lynas.util.AuthUtil
import com.lynas.util.getLogger
import com.lynas.util.responseOK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/15/16
 */

@RestController
@RequestMapping("enrolments")
class EnrolmentController(val enrolmentService: EnrolmentService, val studentService: StudentService,
                          val classService: ClassService, val authUtil: AuthUtil) {

    val log = getLogger(this.javaClass)

    @PostMapping
    fun post(@RequestBody enrolmentDTO: EnrolmentDTO, request: HttpServletRequest): ResponseEntity<*> {
        log.info("Hit in enrolment create method with {}", enrolmentDTO.toString())
        val organizationId = authUtil.getOrganizationIdFromToken(request)
        val (isValid, message) = enrolmentService.studentEnrolmentCheck(
                roleNumber = enrolmentDTO.roleNumber,
                studentId = enrolmentDTO.studentId,
                classId = enrolmentDTO.classId,
                year = enrolmentDTO.year,
                orgId = organizationId)
        if (!isValid) {
            throw DuplicateEntryException(message)
        }
        val student = studentService.findById(enrolmentDTO.studentId, organizationId)
                ?: throw EntityNotFoundException("Student not found with given student id ${enrolmentDTO.studentId}")
        val course = classService.findById(enrolmentDTO.classId, organizationId)
                ?: throw EntityNotFoundException("Class/Course not found with given class/course id"
                        + enrolmentDTO.classId)

        val enrolment: Enrolment = Enrolment(year = enrolmentDTO.year, roleNumber = enrolmentDTO.roleNumber,
                student = student, cls = course)

        enrolmentService.create(enrolment)
        return responseOK(enrolment)
    }

    @DeleteMapping("/{id}/studentId/{stdId}/year/{year}")
    fun delete(@PathVariable id: Long,
               @PathVariable stdId: Long,
               @PathVariable year: Int,
               request: HttpServletRequest): ResponseEntity<*> {
        // TODO is it always be in response ok
        return responseOK(enrolmentService.delete(id, stdId, year, authUtil.getOrganizationIdFromToken(request)))
    }


}