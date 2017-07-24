package com.lynas.controller.rest

import com.lynas.model.Exam
import com.lynas.model.Organization
import com.lynas.model.request.ExamJsonWrapper
import com.lynas.model.response.ExamClassResponse
import com.lynas.service.*
import com.lynas.util.AppConstant
import com.lynas.util.getLogger
import com.lynas.util.getOrganizationFromSession
import com.lynas.util.responseOK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/15/16
 */

@RestController
@RequestMapping("exams")
class ExamRestController(val examService: ExamService,
                         val classService: ClassService,
                         val subjectService: SubjectService,
                         val studentService: StudentService,
                         val examServiceJava: ExamServiceJava) {

    val logger = getLogger(ExamRestController::class.java)

    @PostMapping
    fun post(@RequestBody examJson: ExamJsonWrapper, request: HttpServletRequest): ResponseEntity<*> {
        logger.info("hit in post controller with {}", examJson)
        val organization = getOrganizationFromSession(request)
        val course = classService.findById(examJson.classId, organization.id!!)
        val _subject = subjectService.findById(examJson.subjectId)
        val listOfExam = examJson.examJson.map {
            (mark, studentId, _isPresent) ->
            Exam().apply {
                examType = examJson.examType
                totalNumber = examJson.totalMark
                percentile = examJson.percentile
                isPresent = _isPresent
                year = examJson.year
                cls = course
                subject = _subject
                obtainedNumber = mark
                student = studentService.findById(studentId, organization.id!!)
            }
        }
        //examService.save(listOfExam)
        return responseOK(examJson)
    }

    @GetMapping("/class/{classId}/subject/{subjectId}/year/{_year}/results")
    fun resultOfClassBySubject(@PathVariable classId: Long,
                               @PathVariable subjectId: Long,
                               @PathVariable _year: Int,
                               request: HttpServletRequest): ResponseEntity<*> {
        logger.info("return result of class id {} and subject id {} and year {}", classId, subjectId, _year)
        val organization = getOrganizationFromSession(request)
        return responseOK(examService.resultOfSubjectByYear(classId, subjectId, _year, organization.id!!))
    }

    @GetMapping("student/{studentId}/results")
    fun resultOfStudentByYear(@PathVariable studentId: Long, request: HttpServletRequest): ResponseEntity<*> {
        logger.info("return result for student id [{}]", studentId)
        val organization = request.session.getAttribute(AppConstant.organization) as Organization
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
        val organization = getOrganizationFromSession(request)
        return responseOK(examService.resultOfStudentByYear(classId, studentId, _year, organization.id!!))
    }

    @GetMapping("/class/{classId}/year/{_year}/results")
    fun resultOfClassByYear(@PathVariable classId: Long,
                               @PathVariable _year: Int,
                              request: HttpServletRequest): ResponseEntity<*> {
        logger.info("return result of class id {} and student id {} and year {}", classId, _year)
        val organization = getOrganizationFromSession(request)
        val result = examService.resultOfClass(classId, _year, organization.id!!).groupBy { it.roleNumber }
                .map { ExamClassResponse().apply {
                    roll = it.key
                    name = it.value[0].person
                    resultOfSubjects = it.value.associateBy({it.subject}, {
                        it.exam
                                .map { (it.percentile!! * it.obtainedNumber!!) / 100 }
                                .sum()
                    })
                } }

        return responseOK(examService.resultOfClass(classId, _year, organization.id!!))
    }

    @GetMapping("/class/{classId}/year/{_year}/results/new")
    fun resultOfClass(@PathVariable classId: Long,
                      @PathVariable _year: Int,
                      request: HttpServletRequest): ResponseEntity<*> {
        val organization = getOrganizationFromSession(request)
        logger.info("Hit method with classId [{}], year [{}], organization [{}]", classId, _year, organization.id)
        val start = LocalTime.now()
        val result = examServiceJava.getResultOfClass(classId, _year, organization.id!!)
        val end = LocalTime.now()
        logger.info("Execution Time [{}] millisecond", ChronoUnit.MILLIS.between(start, end))
        return responseOK(result)
    }
}