package com.lynas.controller.rest

import com.lynas.exception.DuplicateEntryException
import com.lynas.exception.EntityNotFoundForGivenIdException
import com.lynas.model.Enrolment
import com.lynas.model.util.EnrolmentJson
import com.lynas.service.ClassService
import com.lynas.service.EnrolmentService
import com.lynas.service.StudentService
import com.lynas.util.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/15/16
 */

@RestController
@RequestMapping("enrolments")
class EnrolmentRestController(val enrolmentService: EnrolmentService,
                              val studentService: StudentService,
                              val classService: ClassService) {

    val logger = getLogger(this.javaClass)

    @PostMapping
    fun post(@RequestBody enrolmentJson: EnrolmentJson, request: HttpServletRequest): ResponseEntity<*> {
        logger.info("Hit in enrolment create method with {}", enrolmentJson.toString())
        val organizationId = getCurrentUserOrganizationId(request)
        val (isValid, message) = enrolmentService.studentEnrolmentCheck(
                roleNumber = enrolmentJson.roleNumber,
                studentId = enrolmentJson.studentId,
                classId = enrolmentJson.classId,
                year = enrolmentJson.year,
                orgId = organizationId)
        if (!isValid) {
            throw DuplicateEntryException(message)
        }
        val _student = studentService.findById(enrolmentJson.studentId, organizationId)
                ?: throw EntityNotFoundForGivenIdException("Student not found with given student id ${enrolmentJson.studentId}")
        val _course = classService.findById(enrolmentJson.classId, organizationId)
                ?: throw EntityNotFoundForGivenIdException("Class/Course not found with given class/course id" + enrolmentJson.classId)

        val enrolment: Enrolment = Enrolment(
                year = enrolmentJson.year,
                roleNumber = enrolmentJson.roleNumber,
                student = _student,
                cls = _course)

        enrolmentService.create(enrolment)
        return responseOK(enrolment)
    }


}