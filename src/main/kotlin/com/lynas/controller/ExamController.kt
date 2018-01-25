package com.lynas.controller

import com.lynas.model.response.ExamClassResponse
import com.lynas.model.util.ExamJsonWrapper
import com.lynas.model.util.ExamUpdateJson
import com.lynas.service.ExamService
import com.lynas.service.ExamServiceJava
import com.lynas.service.StudentService
import com.lynas.util.AuthUtil
import com.lynas.util.getLogger
import com.lynas.util.responseError
import com.lynas.util.responseOK
import org.neo4j.ogm.exception.NotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/15/16
 */

@RestController
@RequestMapping("exams")
class ExamController(val examService: ExamService,
                     val studentService: StudentService,
                     val examServiceJava: ExamServiceJava,
                     val authUtil: AuthUtil) {

    val logger = getLogger(this.javaClass)

    @PostMapping
    fun post(@RequestBody examJson: ExamJsonWrapper, request: HttpServletRequest): ResponseEntity<*> {
        logger.info("hit in create controller with {}", examJson)
        val organization = authUtil.getOrganizationFromToken(request)
        examService.create(examJson, organization)
        return responseOK(examJson)
    }

    @PatchMapping
    fun updateExamMark(@RequestBody examUpdateJson: ExamUpdateJson, request: HttpServletRequest): ResponseEntity<*> {
        logger.info("Update examId [{}], with updated obtain mark [{}]", examUpdateJson.examId, examUpdateJson.updateObtainMark)
        examService.update(examUpdateJson)
        return responseOK(examUpdateJson)

    }

    @GetMapping("/class/{classId}/subject/{subjectId}/year/{_year}/results")
    fun resultOfClassBySubject(@PathVariable classId: Long,
                               @PathVariable subjectId: Long,
                               @PathVariable _year: Int,
                               request: HttpServletRequest): ResponseEntity<*> {
        logger.info("return result of class id {} and subject id {} and year {}", classId, subjectId, _year)
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
        logger.info("return result for student id [{}]", studentId)
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
        logger.info("return result of class id {} and student id {} and year {}", classId, studentId, _year)
        val organization = authUtil.getOrganizationFromToken(request)
        return responseOK(examService.resultOfStudentByYear(classId, studentId, _year, organization.id!!))
    }

    @GetMapping("/class/{classId}/year/{_year}/results")
    fun resultOfClassByYear(@PathVariable classId: Long,
                            @PathVariable _year: Int,
                            request: HttpServletRequest): ResponseEntity<*> {
        logger.info("return result of class id {} and student id {} and year {}", classId, _year)
        val organization = authUtil.getOrganizationFromToken(request)
        // TODO why following code was written
        val result = examService.resultOfClass(classId, _year, organization.id!!).groupBy { it.roleNumber }
                .map {
                    ExamClassResponse().apply {
                        roll = it.key
                        name = it.value[0].person
                        resultOfSubjects = it.value.associateBy({ it.subject }, {
                            it.exam
                                    .map { (it.percentile * it.obtainedNumber) / 100 }
                                    .sum()
                        })
                    }
                }

        return responseOK(examService.resultOfClass(classId, _year, organization.id!!))
    }

    @GetMapping("/class/{classId}/year/{_year}/results/new")
    fun resultOfClass(@PathVariable classId: Long,
                      @PathVariable _year: Int,
                      request: HttpServletRequest): ResponseEntity<*> {
        val organization = authUtil.getOrganizationFromToken(request)
        logger.info("Hit method with classId [{}], year [{}], organization [{}]", classId, _year, organization.id)
        val result = examServiceJava.getResultOfClass(classId, _year, organization.id!!)
        return responseOK(result)
    }
}