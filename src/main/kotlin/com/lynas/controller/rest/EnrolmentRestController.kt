package com.lynas.controller.rest

import com.lynas.model.Course
import com.lynas.model.Enrolment
import com.lynas.model.Student
import com.lynas.model.request.EnrolmentJson
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

    val logger = getLogger(EnrolmentRestController::class.java)

    @PostMapping
    fun post(@RequestBody enrolmentJson: EnrolmentJson, request: HttpServletRequest): ResponseEntity<*> {
        logger.info("Hit in enrolment post method with {}", enrolmentJson.toString())
        val organization = getOrganizationFromSession(request)
        val (enrollment, isEnroll) = enrolmentService.studentEnrolmentCheck(
                enrolmentJson.studentId, enrolmentJson.year, organization.id!!)
        if (isEnroll) {
            // TODO need to change !! line below
            return responseConflict(enrollment!!)
        }

        val _student: Student = studentService.findById(enrolmentJson.studentId, organization.id!!)
        val _course: Course = classService.findById(enrolmentJson.classId, organization.id!!)
                ?: return responseError("Class/Course not found with given class/course id" + enrolmentJson.classId)

        val enrolment: Enrolment = Enrolment().apply {
            year = enrolmentJson.year
            roleNumber = enrolmentJson.roleNumber
            student = _student
            cls = _course
        }

        enrolmentService.save(enrolment)
        return responseOK(enrolment)
    }


}