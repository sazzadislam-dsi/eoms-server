package com.lynas.controller.rest

import com.lynas.model.ContactInformation
import com.lynas.model.Person
import com.lynas.model.Student
import com.lynas.model.request.StudentContact
import com.lynas.model.request.StudentJson
import com.lynas.model.response.ErrorObject
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
    private val logger = getLogger(StudentRestController::class.java)

    @PostMapping
    fun post(@RequestBody studentJson: StudentJson, request: HttpServletRequest): ResponseEntity<*> {
        logger.warn("received student " + studentJson.toString())

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

        val student: Student = Student()
        student.person = Person().apply {
            firstName = studentJson.firstName
            lastName = studentJson.lastName
            dateOfBirth = _dateOfBirth
            sex = studentJson.sex
            religion = studentJson.religion
            organization = getOrganizationFromSession(request)
        }
        studentService.save(student)
        logger.warn("created student " + student.toString())
        return responseOK(student)
    }

    @GetMapping
    fun postStudentWithGivenPerson(): Student {
        val student: Student = Student()
        student.person?.firstName = "ff"
        student.person?.lastName = "L"
        student.person?.dateOfBirth = Date()

        return student
    }

    @PatchMapping
    fun studentUpdate(@RequestBody studentJson: StudentJson): ResponseEntity<*> {
        logger.info("Hit student update controller with id {} && {}", studentJson.studentId, studentJson)
        val student = studentService.findById(studentJson.studentId)
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

        studentService.save(student)

        return responseOK(student)
    }

    @PostMapping("/add_contact_info")
    fun postStudentContactInformation(@RequestBody studentContact: StudentContact): Student {
        val student = studentService.findById(studentContact.studentId)
        if (null == student.person?.contactInformationList) {
            student.person?.contactInformationList = mutableListOf(
                    ContactInformation().apply {
                        name = studentContact.name
                        address = studentContact.address
                        phone_1 = studentContact.phone_1
                        phone_2 = studentContact.phone_2
                        phone_3 = studentContact.phone_3
                        contactType = studentContact.contactType

                    })
        } else {
            student.person?.contactInformationList?.add(ContactInformation().apply {
                name = studentContact.name
                address = studentContact.address
                phone_1 = studentContact.phone_1
                phone_2 = studentContact.phone_2
                phone_3 = studentContact.phone_3
                contactType = studentContact.contactType

            })
        }
        return studentService.save(student)
    }


}