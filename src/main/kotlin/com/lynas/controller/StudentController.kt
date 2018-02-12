package com.lynas.controller

import com.lynas.dto.StudentContactDTO
import com.lynas.dto.StudentDTO
import com.lynas.exception.EntityNotFoundException
import com.lynas.model.ContactInformation
import com.lynas.model.Person
import com.lynas.model.Student
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
class StudentController(val studentService: StudentService, val personService: PersonService, val authUtil: AuthUtil) {
    private val log = getLogger(this.javaClass)

    @PostMapping
    fun post(@RequestBody studentDTO: StudentDTO, request: HttpServletRequest): ResponseEntity<*> {
        log.warn("received student " + studentDTO.toString())

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
    fun studentById(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<*> {
        val orgId = authUtil.getOrganizationIdFromToken(request)
        val student = studentService.findById(id, orgId) ?:
                throw EntityNotFoundException("Student not found for given id [$id]")
        student.person.contactInformationList = personService.findPersonById(student.person.id!!)
                .contactInformationList
        return responseOK(student)
    }

    @GetMapping("/count")
    fun totalStudentCount(request: HttpServletRequest): Int {
        return studentService.findStudentCountOfOrganization(authUtil.getOrganizationIdFromToken(request))
    }

    @GetMapping("/list")
    fun studentList(request: HttpServletRequest)
            = studentService.findAll(authUtil.getOrganizationIdFromToken(request))

    @PatchMapping
    fun studentUpdate(@RequestBody studentDTO: StudentDTO,
                      request: HttpServletRequest): ResponseEntity<*> {
        log.info("Hit student update controller with id {} && {}", studentDTO.studentId, studentDTO)
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
    fun postStudentContactInformation(@RequestBody studentContactDTO: StudentContactDTO,
                                      request: HttpServletRequest): ResponseEntity<*> {
        val student = studentService.findById(studentContactDTO.studentId, authUtil.getOrganizationIdFromToken(request))
                ?: return responseError(
                        "Student not found with given student id ${studentContactDTO.studentId}")
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
    fun studentSearchWithName(@RequestBody name: String, request: HttpServletRequest): ResponseEntity<*> {
        if (name.isEmpty()) {
            log.info("student search with Empty string")
            return responseError("")
        }
        log.info("search with student name [{}]", name)
        val organization = authUtil.getOrganizationFromToken(request)
        val studentList = studentService.searchByFirstName(name, organization.id!!)
        return responseOK(studentList)

    }
}