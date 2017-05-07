package com.lynas.controller.view

import com.fasterxml.jackson.databind.ObjectMapper
import com.lynas.model.Organization
import com.lynas.service.PersonService
import com.lynas.model.Student
import com.lynas.service.StudentService
import com.lynas.util.AppConstant
import com.lynas.util.getLogger
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/15/16
 */

@Controller
@RequestMapping("student")
class StudentController(val studentService: StudentService, val personService: PersonService) {

    val logger = getLogger(StudentController::class.java)

    @GetMapping("/create")
    fun studentCreate(): String {
        logger.info("return studentCreate page")
        return "studentCreate"
    }


    @GetMapping("/{studentId}/update")
    fun studentUpdate(model: Model, @PathVariable studentId: Long, request: HttpServletRequest): String {
        val organization = request.session.getAttribute(AppConstant.organization) as Organization
        model.addAttribute("student", studentService.findById(studentId, organization.name))
        return "studentUpdate"
    }


    @GetMapping("/search")
    fun studentSearch(): String {
        return "studentSearch"
    }

    @GetMapping("/search/byFirstName")
    fun studentSearch(@RequestParam firstName: String, model: Model, request: HttpServletRequest): String {
        logger.info("Student first name {}", firstName)
        val organization = request.session.getAttribute(AppConstant.organization) as Organization
        val list: List<Student> = studentService.searchByFirstName(firstName, organization.name)
        model.addAttribute("studentList", list)
        return "studentSearch"
    }

    @GetMapping("/{studentId}/details")
    fun viewDetails(@PathVariable studentId: Long, model: Model, request: HttpServletRequest): String {
        val organization = request.session.getAttribute(AppConstant.organization) as Organization
        val student = studentService.findById(studentId, organization.name)
        student.person?.contactInformationList = personService.findPersonById(student.person?.id!!).contactInformationList
        model.addAttribute("student", student)
        model.addAttribute("studentJson", ObjectMapper().writeValueAsString(student))
        println(student.toString())
        return "studentDetails"
    }

    @GetMapping("/{studentId}/addContactInformation")
    fun addContactInformation(@PathVariable studentId: Long, model: Model): String {
        model.addAttribute("studentId", studentId)
        return "studentContactInformationAdd"
    }

    @GetMapping("/list")
    fun studentList(model: Model, request: HttpServletRequest): String {
        val organization = request.session.getAttribute(AppConstant.organization) as Organization
        model.addAttribute("studentList", studentService.findAll(organization.name))
        return "studentList"
    }

    /*@GetMapping("/detail/{contactId}/contactInfo")
    fun findbyStudentOfContactId(@PathVariable contactId: Long): String {
        logger.info("contact id {} for searching student", contactId)
        var student = studentService.findStudentByContactId(contactId)
        return "redirect:/student/" + student.id + "/details"
    }*/

}























