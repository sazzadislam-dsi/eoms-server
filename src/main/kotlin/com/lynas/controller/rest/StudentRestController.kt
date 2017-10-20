package com.lynas.controller.rest

import com.lynas.model.ContactInformation
import com.lynas.model.Person
import com.lynas.model.Student
import com.lynas.model.response.ErrorObject
import com.lynas.model.util.StudentContact
import com.lynas.model.util.StudentJson
import com.lynas.service.StudentService
import com.lynas.util.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/15/16
 */

@RestController
@RequestMapping("students")
class StudentRestController(val studentService: StudentService) {
    private val logger = getLogger(this.javaClass)

    @PostMapping
    fun post(@RequestBody studentJson: StudentJson, request: HttpServletRequest): ResponseEntity<*> {
        logger.warn("received student " + studentJson.toString())

        val _dateOfBirth: Date
        try {
            _dateOfBirth = studentJson.dateOfBirth.convertToDate()
        } catch (ex: Exception) {
            ex.printStackTrace()
            return responseError(ErrorObject(
                    studentJson,
                    "dateOfBirth",
                    Constants.INVALID_DATE_FORMAT,
                    Constants.EXPECTED_DATE_FORMAT))
        }

        val student = Student()
        student.person = Person(
                firstName = studentJson.firstName,
                lastName = studentJson.lastName,
                dateOfBirth = _dateOfBirth,
                sex = studentJson.sex,
                religion = studentJson.religion,
                organization = getOrganizationFromSession(request),
                contactInformationList = mutableListOf())
        studentService.create(student)
        logger.warn("created student " + student.toString())
        return responseOK(student)
    }

    @GetMapping
    fun get(): Student {
        val student = Student()
        student.person?.firstName = "ff"
        student.person?.lastName = "L"
        student.person?.dateOfBirth = Date()

        return student
    }

    @GetMapping("/count")
    fun totalStudentCount(request: HttpServletRequest): ResponseEntity<*> {
        val studentCount = studentService.findStudentCountOfOrganization(getCurrentUserOrganizationId(request))
        return responseOK(studentCount)
    }

    @PatchMapping
    fun studentUpdate(@RequestBody studentJson: StudentJson,
                      request: HttpServletRequest): ResponseEntity<*> {
        logger.info("Hit student update controller with id {} && {}", studentJson.studentId, studentJson)
        val student = studentService.findById(studentJson.studentId, getCurrentUserOrganizationId(request))
        if (student?.person == null) return responseError(studentJson)

        var _dateOfBirth = Date()
        try {
            _dateOfBirth = studentJson.dateOfBirth.convertToDate()
        } catch (ex: Exception) {
            ex.printStackTrace()
            return responseError(ErrorObject(
                    studentJson,
                    "dateOfBirth",
                    Constants.INVALID_DATE_FORMAT,
                    Constants.EXPECTED_DATE_FORMAT))
        }

        student.person?.apply {
            firstName = studentJson.firstName
            lastName = studentJson.lastName
            dateOfBirth = _dateOfBirth
            sex = studentJson.sex
            religion = studentJson.religion
        }

        studentService.create(student)

        return responseOK(student)
    }

    @PostMapping("/add_contact_info")
    fun postStudentContactInformation(@RequestBody studentContact: StudentContact,
                                      request: HttpServletRequest): ResponseEntity<*> {
        val student = studentService.findById(studentContact.studentId, getCurrentUserOrganizationId(request))
                ?: return responseError("Student not found with given student id ${studentContact.studentId}")
        if (null == student.person?.contactInformationList) {
            student.person?.contactInformationList = mutableListOf(
                    ContactInformation(
                            name = studentContact.name,
                            address = studentContact.address,
                            phone_1 = studentContact.phone_1,
                            phone_2 = studentContact.phone_2,
                            phone_3 = studentContact.phone_3,
                            contactType = studentContact.contactType))
        } else {
            student.person?.contactInformationList?.add(ContactInformation(
                    name = studentContact.name,
                    address = studentContact.address,
                    phone_1 = studentContact.phone_1,
                    phone_2 = studentContact.phone_2,
                    phone_3 = studentContact.phone_3,
                contactType = studentContact.contactType

            ))
        }
        return responseOK(studentService.create(student))
    }

    @PostMapping("/search/name")
    fun studentSearchWithName(@RequestBody name: String, request: HttpServletRequest): ResponseEntity<*> {
        if (name.isEmpty()) {
            logger.info("student search with Empty string")
            return responseError("")
        }
        logger.info("search with student name [{}]", name)
        val organization = getOrganizationFromSession(request)
        val studentList = studentService.searchByFirstName(name, organization.id!!)
        return responseOK(studentList)

    }


}