package com.lynas.controller

import com.lynas.dto.EnrolmentDTO
import com.lynas.exception.DuplicateEntryException
import com.lynas.model.Enrolment
import com.lynas.service.ClassService
import com.lynas.service.EnrolmentService
import com.lynas.service.StudentService
import com.lynas.util.AuthUtil
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

    @PostMapping
    fun createNewEnrolment(@RequestBody enrolmentDTO: EnrolmentDTO, request: HttpServletRequest): ResponseEntity<*> {
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
        val course = classService.findById(enrolmentDTO.classId, organizationId)

        val enrolment = Enrolment(year = enrolmentDTO.year, roleNumber = enrolmentDTO.roleNumber,
                student = student, cls = course)

        enrolmentService.create(enrolment)
        return responseOK(enrolment)
    }

    @DeleteMapping("/{id}/studentId/{stdId}/year/{year}")
    fun deleteEnrolment(@PathVariable id: Long,
               @PathVariable stdId: Long,
               @PathVariable year: Int,
               request: HttpServletRequest): ResponseEntity<*> {
        return responseOK(enrolmentService.delete(id, stdId, year, authUtil.getOrganizationIdFromToken(request)))
    }


}