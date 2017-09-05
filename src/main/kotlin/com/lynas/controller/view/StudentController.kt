package com.lynas.controller.view

import com.fasterxml.jackson.databind.ObjectMapper
import com.lynas.model.Student
import com.lynas.model.response.ExamClassResponse1
import com.lynas.service.ExamServiceJava
import com.lynas.service.FeeInfoService
import com.lynas.service.PersonService
import com.lynas.service.StudentService
import com.lynas.util.convertToString
import com.lynas.util.getLogger
import com.lynas.util.getOrganizationFromSession
import org.neo4j.ogm.exception.NotFoundException
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/15/16
 */

@Controller
@RequestMapping("student")
class StudentController(val studentService: StudentService,
                        val personService: PersonService,
                        val feeInfoService: FeeInfoService,
                        val examServiceJava: ExamServiceJava) {

    val logger = getLogger(StudentController::class.java)

    @GetMapping("/create")
    fun studentCreate(): String {
        logger.info("return studentCreate page")
        return "studentCreate"
    }


    @GetMapping("/{studentId}/update")
    fun studentUpdate(model: Model, @PathVariable studentId: Long, request: HttpServletRequest): String {
        val organization = getOrganizationFromSession(request)
        val student = studentService.findById(studentId, organization.id!!)
        student?.person?.dateInString = student?.person?.dateOfBirth?.convertToString()
        model.addAttribute("student", student)
        return "studentUpdate"
    }


    @GetMapping("/search")
    fun studentSearch(): String {
        return "studentSearch"
    }

    @GetMapping("/search/byFirstName")
    fun studentSearch(@RequestParam firstName: String, model: Model, request: HttpServletRequest): String {
        logger.info("Student first name {}", firstName)
        val organization = getOrganizationFromSession(request)
        val list: List<Student> = studentService.searchByFirstName(firstName, organization.id!!)
        model.addAttribute("studentList", list)
        return "studentSearch"
    }

    @GetMapping("/{studentId}/details")
    fun viewDetails(@PathVariable studentId: Long, model: Model, request: HttpServletRequest): String {
        val organization = getOrganizationFromSession(request)
        val student = studentService.findById(studentId, organization.id!!) ?: throw NotFoundException()
        student.person?.contactInformationList = personService.findPersonById(student.person?.id!!)?.contactInformationList
        model.addAttribute("student", student)
        model.addAttribute("studentJson", ObjectMapper().writeValueAsString(student))
        val studentFeeList = feeInfoService.findStudentFeeInfoByStudent(studentId)
                ?.filter { it.feeInfo?.course?.organization?.id == getOrganizationFromSession(request).id }
        model.addAttribute("studentFeeList", studentFeeList)
        return "studentDetails"
    }

    @GetMapping("/{studentId}/details/class/{classId}/year/{year}")
    fun studentViewDetails(model: Model, request: HttpServletRequest,
                           @PathVariable studentId: Long,
                           @PathVariable year: Int,
                           @PathVariable classId: Long): String {
        val organization = getOrganizationFromSession(request)
        val student = studentService.findById(studentId, organization.id!!) ?: throw NotFoundException()
        student.person?.contactInformationList = personService.findPersonById(student.person?.id!!)?.contactInformationList
        model.addAttribute("student", student)
        model.addAttribute("studentJson", ObjectMapper().writeValueAsString(student))
        val studentFeeList = feeInfoService.findStudentFeeInfoByStudent(studentId)
                ?.filter { it.feeInfo?.course?.organization?.id == organization.id }
        model.addAttribute("studentFeeList", studentFeeList)

        val resultList = examServiceJava.getResultOfClass(classId, year, organization.id!!)
        var result: ExamClassResponse1? = null
        if (!resultList.isEmpty()) {
            result = resultList
                    .filter { it.studentId == studentId }
                    .first()
        }

        model.addAttribute("result", result)

        return "studentDetails"
    }

    @GetMapping("/{studentId}/addContactInformation")
    fun addContactInformation(@PathVariable studentId: Long, model: Model): String {
        model.addAttribute("studentId", studentId)
        return "studentContactInformationAdd"
    }

    @GetMapping("/list")
    fun studentList(model: Model, request: HttpServletRequest): String {
        val organization = getOrganizationFromSession(request)
        model.addAttribute("studentList", studentService.findAll(organization.id!!))
        return "studentList"
    }

    /*@GetMapping("/detail/{contactId}/contactInfo")
    fun findbyStudentOfContactId(@PathVariable contactId: Long): String {
        logger.info("contact id {} for searching student", contactId)
        var student = studentService.findStudentByContactId(contactId)
        return "redirect:/student/" + student.id + "/details"
    }*/

}























