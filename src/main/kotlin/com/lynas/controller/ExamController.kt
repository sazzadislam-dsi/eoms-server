package com.lynas.controller

import com.lynas.dto.ExamJsonWrapper
import com.lynas.dto.ExamUpdateDTO
import com.lynas.model.util.ExamType
import com.lynas.service.ExamService
import com.lynas.service.ExamServiceJava
import com.lynas.service.StudentService
import com.lynas.util.AuthUtil
import com.lynas.util.convertToDate
import com.lynas.util.responseOK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/15/16
 */

@RestController
@RequestMapping("exams")
class ExamController(val examService: ExamService, val studentService: StudentService,
                     val examServiceJava: ExamServiceJava, val authUtil: AuthUtil) {

    @PostMapping
    fun createNewExam(@RequestBody examJson: ExamJsonWrapper, request: HttpServletRequest): ResponseEntity<*> {
        val organization = authUtil.getOrganizationFromToken(request)
        examService.create(examJson, organization)
        return responseOK(examJson)
    }

    @PatchMapping
    fun updateExamMark(@RequestBody examUpdateDTO: ExamUpdateDTO, request: HttpServletRequest): ResponseEntity<*> {
        examService.update(examUpdateDTO)
        return responseOK(examUpdateDTO)
    }

    @GetMapping("/class/{classId}/subject/{subjectId}/year/{_year}/results")
    fun getResultOfExamOfClassBySubject(@PathVariable classId: Long, @PathVariable subjectId: Long,
                               @PathVariable _year: Int, request: HttpServletRequest): ResponseEntity<*> {
        return responseOK(examService.resultOfSubjectByYear(classId = classId,subjectId = subjectId,
                                                _year = _year, orgId = authUtil.getOrganizationIdFromToken(request)))
    }

    @GetMapping("student/{studentId}/results")
    fun getExamResultOfStudentByYear(@PathVariable studentId: Long, request: HttpServletRequest): ResponseEntity<*> {
        val organization = authUtil.getOrganizationFromToken(request)
        val year = LocalDate.now().year
        val studentInfo = studentService.studentInfoByYear(id = studentId, year = year, orgId = organization.id!!)
        return responseOK(examService.resultOfStudentByYear(studentInfo.classId, studentId, year, organization.id!!))
    }

    @GetMapping("/class/{classId}/student/{studentId}/year/{_year}/results")
    fun getExamResultOfStudentByYear(@PathVariable classId: Long,
                              @PathVariable studentId: Long,
                              @PathVariable _year: Int,
                              request: HttpServletRequest): ResponseEntity<*> {
        val organization = authUtil.getOrganizationFromToken(request)
        return responseOK(examService.resultOfStudentByYear(classId, studentId, _year, organization.id!!))
    }

    @GetMapping("/class/{classId}/year/{_year}/results")
    fun getExamResultOfClassByYear(@PathVariable classId: Long,
                            @PathVariable _year: Int,
                            request: HttpServletRequest): ResponseEntity<*> {
        val organization = authUtil.getOrganizationFromToken(request)
        return responseOK(examService.resultOfClass(classId, _year, organization.id!!))
    }

    @GetMapping("/class/{classId}/year/{_year}/results/new")
    fun getExamResultOfClass(@PathVariable classId: Long,
                      @PathVariable _year: Int,
                      request: HttpServletRequest): ResponseEntity<*> {
        val organization = authUtil.getOrganizationFromToken(request)
        val result = examServiceJava.getResultOfClass(classId, _year, organization.id!!)
        return responseOK(result)
    }

    @GetMapping("/taken_exam_list/class/{classId}/subject/{subjectId}/year/{_year}")
    fun getExamTakenOfSubject(@PathVariable classId: Long, @PathVariable subjectId: Long, @PathVariable _year: Int,
                           request: HttpServletRequest)
            = responseOK(examService.examListOfSubject(classId, subjectId, _year,
            authUtil.getOrganizationIdFromToken(request)))

    @GetMapping(
            "/result/update/classId/{classId}/subjectId/{subjectId}/year/{_year}/date/{date}/examType/{examType}")
    fun getSubjectResultByDate(@PathVariable classId: Long, @PathVariable subjectId: Long, @PathVariable _year: Int,
                               @PathVariable date: String, @PathVariable examType: ExamType,
                               request: HttpServletRequest): ResponseEntity<*> {
        val convertedDate: Date = date.convertToDate()
        return responseOK(examService.findByClassIdSubjectIdYearDateExamType(
                classId, subjectId, _year, convertedDate, examType, authUtil.getOrganizationIdFromToken(request)))
    }

    @GetMapping("/classId/{classId}/year/{year}/results")
    fun getResultOfClass(@PathVariable classId: Long, @PathVariable year: Int, request: HttpServletRequest)
            = responseOK(examServiceJava.getResultOfClass(classId, year, authUtil.getOrganizationIdFromToken(request)))
}