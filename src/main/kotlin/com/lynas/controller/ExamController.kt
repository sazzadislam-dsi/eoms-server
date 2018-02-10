package com.lynas.controller

import com.lynas.dto.ExamJsonWrapper
import com.lynas.dto.ExamUpdateDTO
import com.lynas.model.util.ExamType
import com.lynas.service.ExamService
import com.lynas.service.ExamServiceJava
import com.lynas.service.StudentService
import com.lynas.util.*
import org.neo4j.ogm.exception.NotFoundException
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

    private val log = getLogger(this.javaClass)

    @PostMapping
    fun post(@RequestBody examJson: ExamJsonWrapper, request: HttpServletRequest): ResponseEntity<*> {
        log.info("hit in create controller with {}", examJson)
        val organization = authUtil.getOrganizationFromToken(request)
        examService.create(examJson, organization)
        return responseOK(examJson)
    }

    @PatchMapping
    fun updateExamMark(@RequestBody examUpdateDTO: ExamUpdateDTO, request: HttpServletRequest): ResponseEntity<*> {
        log.info("Update examId [{}], with updated obtain mark [{}]", examUpdateDTO.examId,
                examUpdateDTO.updateObtainMark)
        examService.update(examUpdateDTO)
        return responseOK(examUpdateDTO)

    }

    @GetMapping("/class/{classId}/subject/{subjectId}/year/{_year}/results")
    fun resultOfClassBySubject(@PathVariable classId: Long, @PathVariable subjectId: Long,
                               @PathVariable _year: Int, request: HttpServletRequest): ResponseEntity<*> {
        log.info("return result of class id {} and subject id {} and year {}", classId, subjectId, _year)
        return try {
            responseOK(examService.resultOfSubjectByYear(
                    classId = classId,
                    subjectId = subjectId,
                    _year = _year,
                    orgId = authUtil.getOrganizationIdFromToken(request)))
        } catch (e: NotFoundException) {
            responseError(e.message
                    ?: "(ExamController:resultOfClassBySubject:NotFoundException) Error check server")
        } catch (e: Exception) {
            responseError(e.message
                    ?: "(ExamController:resultOfClassBySubject:Exception) Error check server")
        }
    }

    @GetMapping("student/{studentId}/results")
    fun resultOfStudentByYear(@PathVariable studentId: Long, request: HttpServletRequest): ResponseEntity<*> {
        log.info("return result for student id [{}]", studentId)
        val organization = authUtil.getOrganizationFromToken(request)
        val year = LocalDate.now().year
        val studentInfo = studentService.studentInfoByYear(id = studentId, year = year, orgId = organization.id!!)
        return responseOK(examService.resultOfStudentByYear(studentInfo.classId, studentId, year, organization.id!!))
    }

    @GetMapping("/class/{classId}/student/{studentId}/year/{_year}/results")
    fun resultOfStudentByYear(@PathVariable classId: Long,
                              @PathVariable studentId: Long,
                              @PathVariable _year: Int,
                              request: HttpServletRequest): ResponseEntity<*> {
        log.info("return result of class id {} and student id {} and year {}", classId, studentId, _year)
        val organization = authUtil.getOrganizationFromToken(request)
        return responseOK(examService.resultOfStudentByYear(classId, studentId, _year, organization.id!!))
    }

    @GetMapping("/class/{classId}/year/{_year}/results")
    fun resultOfClassByYear(@PathVariable classId: Long,
                            @PathVariable _year: Int,
                            request: HttpServletRequest): ResponseEntity<*> {
        log.info("return result of class id {} and student id {} and year {}", classId, _year)
        val organization = authUtil.getOrganizationFromToken(request)
        return responseOK(examService.resultOfClass(classId, _year, organization.id!!))
    }

    @GetMapping("/class/{classId}/year/{_year}/results/new")
    fun resultOfClass(@PathVariable classId: Long,
                      @PathVariable _year: Int,
                      request: HttpServletRequest): ResponseEntity<*> {
        val organization = authUtil.getOrganizationFromToken(request)
        log.info("Hit method with classId [{}], year [{}], organization [{}]", classId, _year, organization.id)
        val result = examServiceJava.getResultOfClass(classId, _year, organization.id!!)
        return responseOK(result)
    }

    @GetMapping("/taken_exam_list/class/{classId}/subject/{subjectId}/year/{_year}")
    fun examTakenOfSubject(@PathVariable classId: Long, @PathVariable subjectId: Long, @PathVariable _year: Int,
                           request: HttpServletRequest)
            = responseOK(examService.examListOfSubject(classId, subjectId, _year,
            authUtil.getOrganizationIdFromToken(request)))

    @GetMapping(
            "/result/update/classId/{classId}/subjectId/{subjectId}/year/{_year}/date/{date}/examType/{examType}")
    fun getSubjectResultByDate(@PathVariable classId: Long, @PathVariable subjectId: Long, @PathVariable _year: Int,
                               @PathVariable date: String, @PathVariable examType: ExamType,
                               request: HttpServletRequest): ResponseEntity<*> {
        log.info("Result update for classId [{}], subjectId [{}], year [{}], date [{}], examType [{}]",
                classId, subjectId, _year, date, examType)
        val date: Date = date.convertToDate()
        return responseOK(examService.findByClassIdSubjectIdYearDateExamType(
                classId, subjectId, _year, date, examType, authUtil.getOrganizationIdFromToken(request)))
    }

    @GetMapping("/classId/{classId}/year/{year}/results")
    fun getResultOfClass(@PathVariable classId: Long, @PathVariable year: Int, request: HttpServletRequest)
            = responseOK(examServiceJava.getResultOfClass(classId, year, authUtil.getOrganizationIdFromToken(request)))
}