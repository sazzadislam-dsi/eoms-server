package com.lynas.controller

import com.lynas.dto.StudentContactDTO
import com.lynas.dto.StudentDTO
import com.lynas.model.ContactInformation
import com.lynas.model.Person
import com.lynas.model.Student
import com.lynas.service.PersonService
import com.lynas.service.StudentService
import com.lynas.util.AuthUtil
import com.lynas.util.convertToDate
import com.lynas.util.responseError
import com.lynas.util.responseOK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/15/16
 */

@RestController
@RequestMapping("students")
class StudentController(val studentService: StudentService, val personService: PersonService, val authUtil: AuthUtil) {

    @PostMapping
    fun createNewStudent(@RequestBody studentDTO: StudentDTO, request: HttpServletRequest): ResponseEntity<*> {
        val dateOfBirth: Date = studentDTO.dateOfBirth.convertToDate()

        val person = Person(
                firstName = studentDTO.firstName,
                lastName = studentDTO.lastName,
                dateOfBirth = dateOfBirth,
                sex = studentDTO.sex,
                religion = studentDTO.religion,
                organization = authUtil.getOrganizationFromToken(request),
                contactInformationList = mutableListOf())
        val student = Student(person = person, firstAdmissionDate = Date())
        studentService.create(student)
        return responseOK(student)
    }

    @GetMapping("/{id}")
    fun getStudentById(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<*> {
        val orgId = authUtil.getOrganizationIdFromToken(request)
        val student = studentService.findById(id, orgId)
        val studentContactInformationList: MutableList<ContactInformation>?
                = personService.findPersonById(student.person.id!!).contactInformationList
        student.person.contactInformationList = studentContactInformationList ?: mutableListOf()
        return responseOK(student)
    }

    @GetMapping("/count")
    fun getTotalStudentCount(request: HttpServletRequest): Int {
        return studentService.findStudentCountOfOrganization(authUtil.getOrganizationIdFromToken(request))
    }

    @GetMapping("/list")
    fun getStudentList(request: HttpServletRequest)
            = studentService.findAll(authUtil.getOrganizationIdFromToken(request))

    @PatchMapping
    fun studentUpdate(@RequestBody studentDTO: StudentDTO,
                      request: HttpServletRequest): ResponseEntity<*> {
        val student = studentService.findById(studentDTO.studentId, authUtil.getOrganizationIdFromToken(request))
        if (student?.person == null) return responseError(studentDTO)

        val dob = studentDTO.dateOfBirth.convertToDate()

        student.person.apply {
            firstName = studentDTO.firstName
            lastName = studentDTO.lastName
            dateOfBirth = dob
            sex = studentDTO.sex
            religion = studentDTO.religion
        }

        studentService.create(student)

        return responseOK(student)
    }

    @PostMapping("/add_contact_info")
    fun addStudentContactInformation(@RequestBody studentContactDTO: StudentContactDTO,
                                      request: HttpServletRequest): ResponseEntity<*> {
        val student = studentService.findById(studentContactDTO.studentId, authUtil.getOrganizationIdFromToken(request))
        if (student.person.contactInformationList == null) {
            student.person.contactInformationList = mutableListOf()
        }
        student.person.contactInformationList.add(ContactInformation(
                name = studentContactDTO.name,
                address = studentContactDTO.address,
                phone_1 = studentContactDTO.phone_1,
                phone_2 = studentContactDTO.phone_2,
                phone_3 = studentContactDTO.phone_3,
                contactType = studentContactDTO.contactType

        ))
        return responseOK(studentService.create(student))
    }

    @PostMapping("/search/name")
    fun studentSearchByName(@RequestBody name: String, request: HttpServletRequest): ResponseEntity<*> {
        if (name.isEmpty()) {
            return responseError("")
        }
        val organization = authUtil.getOrganizationFromToken(request)
        val studentList = studentService.searchByFirstName(name, organization.id!!)
        return responseOK(studentList)

    }
}