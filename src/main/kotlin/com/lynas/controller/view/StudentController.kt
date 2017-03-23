package com.lynas.controller.view

import com.fasterxml.jackson.databind.ObjectMapper
import com.lynas.service.PersonService
import com.lynas.model.Student
import com.lynas.service.StudentService
import com.lynas.util.getLogger
import com.lynas.util.str
import org.slf4j.Logger
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

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
    fun studentUpdate(model: Model, @PathVariable studentId: Long): String {
        model.addAttribute("student", studentService.findById(studentId))
        return "studentUpdate"
    }


    @GetMapping("/search")
    fun studentSearch(): String {
        return "studentSearch"
    }

    @GetMapping("/search/byFirstName")
    fun studentSearch(@RequestParam firstName: String, model: Model): String {
        logger.info("Student first name {}", firstName)
        val list: List<Student> = studentService.searchByFirstName(firstName)
        model.addAttribute("studentList", list)
        return "studentSearch"
    }

    @GetMapping("/{studentId}/details")
    fun viewDetails(@PathVariable studentId: Long, model: Model): String {
        val student = studentService.findById(studentId)
        val person = student.person
        student.person?.contactInformationList = personService.findPersonById(student.person?.id!!).contactInformationList
        model.addAttribute("student", student)
        model.addAttribute("studentDob", person?.dateOfBirth?.str())
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
    fun studentList(model: Model): String {
        model.addAttribute("studentList", studentService.findAll())
        return "studentList"
    }

    /*@GetMapping("/detail/{contactId}/contactInfo")
    fun findbyStudentOfContactId(@PathVariable contactId: Long): String {
        logger.info("contact id {} for searching student", contactId)
        var student = studentService.findStudentByContactId(contactId)
        return "redirect:/student/" + student.id + "/details"
    }*/

}























