package com.lynas.controller

import com.lynas.exception.EntityNotFoundException
import com.lynas.model.ContactInformation
import com.lynas.model.Person
import com.lynas.model.Student
import com.lynas.model.response.ErrorObject
import com.lynas.model.util.StudentContact
import com.lynas.model.util.StudentJson
import com.lynas.service.PersonService
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
class StudentController(val studentService: StudentService,
                        val personService: PersonService,
                        val authUtil: AuthUtil ) {
    private val logger = getLogger(this.javaClass)

    @PostMapping
    fun post(@RequestBody studentJson: StudentJson, request: HttpServletRequest): ResponseEntity<*> {
        logger.warn("received student " + studentJson.toString())

        val _dateOfBirth: Date = studentJson.dateOfBirth.convertToDate()

        val person = Person(
                firstName = studentJson.firstName,
                lastName = studentJson.lastName,
                dateOfBirth = _dateOfBirth,
                sex = studentJson.sex,
                religion = studentJson.religion,
                organization = authUtil.getOrganizationFromToken(request),
                contactInformationList = mutableListOf())
        val student = Student(person = person, firstAdmissionDate = Date())
        studentService.create(student)
        return responseOK(student)
    }

    @GetMapping("/{id}")
    fun studentById(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<*> {
        val orgId = authUtil.getOrganizationIdFromToken(request)
        val student = studentService.findById(id, orgId) ?:
                throw EntityNotFoundException("Student not found for given id [$id]")
        student.person.contactInformationList = personService.findPersonById(student.person.id!!)?.contactInformationList
                ?: mutableListOf()
        return responseOK(student)
    }

    @GetMapping("/count")
    fun totalStudentCount(request: HttpServletRequest): Int {
        return studentService.findStudentCountOfOrganization(authUtil.getOrganizationIdFromToken(request))
    }

    @PatchMapping
    fun studentUpdate(@RequestBody studentJson: StudentJson,
                      request: HttpServletRequest): ResponseEntity<*> {
        logger.info("Hit student update controller with id {} && {}", studentJson.studentId, studentJson)
        val student = studentService.findById(studentJson.studentId, authUtil.getOrganizationIdFromToken(request))
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

        student.person.apply {
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
        val student = studentService.findById(studentContact.studentId, authUtil.getOrganizationIdFromToken(request))
                ?: return responseError("Student not found with given student id ${studentContact.studentId}")
        if (student.person.contactInformationList == null) {
            student.person.contactInformationList = mutableListOf()
        }
        student.person.contactInformationList.add(ContactInformation(
                name = studentContact.name,
                address = studentContact.address,
                phone_1 = studentContact.phone_1,
                phone_2 = studentContact.phone_2,
                phone_3 = studentContact.phone_3,
                contactType = studentContact.contactType

        ))
        return responseOK(studentService.create(student))
    }

    @PostMapping("/search/name")
    fun studentSearchWithName(@RequestBody name: String, request: HttpServletRequest): ResponseEntity<*> {
        if (name.isEmpty()) {
            logger.info("student search with Empty string")
            return responseError("")
        }
        logger.info("search with student name [{}]", name)
        val organization = authUtil.getOrganizationFromToken(request)
        val studentList = studentService.searchByFirstName(name, organization.id!!)
        return responseOK(studentList)

    }


}