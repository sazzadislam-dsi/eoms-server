package com.lynas.controller.rest

import com.lynas.model.Exam
import com.lynas.model.Organization
import com.lynas.model.request.ExamJsonWrapper
import com.lynas.model.response.ExamClassResponse
import com.lynas.service.ClassService
import com.lynas.service.ExamService
import com.lynas.service.StudentService
import com.lynas.service.SubjectService
import com.lynas.util.AppConstant
import com.lynas.util.getLogger
import com.lynas.util.responseOK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/15/16
 */

@RestController
@RequestMapping("exams")
class ExamRestController(val examService: ExamService,
                         val classService: ClassService,
                         val subjectService: SubjectService,
                         val studentService: StudentService) {

    val logger = getLogger(ExamRestController::class.java)

    @PostMapping
    fun post(@RequestBody examJson: ExamJsonWrapper, request: HttpServletRequest): ResponseEntity<*> {
        logger.info("hit in post controller with {}", examJson)
        val organization = request.session.getAttribute(AppConstant.organization) as Organization
        val course = classService.findById(examJson.classId, organization.name)
        val _subject = subjectService.findById(examJson.subjectId)
        val listOfExam = examJson.examJson.map {
            (mark, studentId) ->
            Exam().apply {
                examType = examJson.examType
                totalNumber = examJson.totalMark
                percentile = examJson.percentile
                year = examJson.year
                cls = course
                subject = _subject
                obtainedNumber = mark
                student = studentService.findById(studentId, organization.name)
            }
        }
        examService.save(listOfExam)
        return responseOK(examJson)
    }

    @GetMapping("/class/{classId}/subject/{subjectId}/year/{_year}/results")
    fun resultOfClassBySubject(@PathVariable classId: Long,
                               @PathVariable subjectId: Long,
                               @PathVariable _year: Int,
                               request: HttpServletRequest): ResponseEntity<*> {
        logger.info("return result of class id {} and subject id {} and year {}", classId, subjectId, _year)
        val organization = request.session.getAttribute(AppConstant.organization) as Organization
        return responseOK(examService.resultOfSubjectByYear(classId, subjectId, _year, organization.name))
    }

    @GetMapping("student/{studentId}/results")
    fun resultOfStudentByYear(@PathVariable studentId: Long, request: HttpServletRequest): ResponseEntity<*> {
        logger.info("return result for student id [{}]", studentId)
        val organization = request.session.getAttribute(AppConstant.organization) as Organization
        val year = LocalDate.now().year
        val studentInfo = studentService.studentInfoByYear(id = studentId, year = year, organization = organization.name)
        return responseOK(examService.resultOfStudentByYear(studentInfo.classId, studentId, year, organization.name))
    }

    @GetMapping("/class/{classId}/student/{studentId}/year/{_year}/results")
    fun resultOfStudentByYear(@PathVariable classId: Long,
                               @PathVariable studentId: Long,
                               @PathVariable _year: Int,
                              request: HttpServletRequest): ResponseEntity<*> {
        logger.info("return result of class id {} and student id {} and year {}", classId, studentId, _year)
        val organization = request.session.getAttribute(AppConstant.organization) as Organization
        return responseOK(examService.resultOfStudentByYear(classId, studentId, _year, organization.name))
    }

    @GetMapping("/class/{classId}/year/{_year}/results")
    fun resultOfClassByYear(@PathVariable classId: Long,
                               @PathVariable _year: Int,
                              request: HttpServletRequest): ResponseEntity<*> {
        logger.info("return result of class id {} and student id {} and year {}", classId, _year)
        val organization = request.session.getAttribute(AppConstant.organization) as Organization
        val result = examService.resultOfClass(classId, _year, organization.name).groupBy { it.roleNumber }
                .map { ExamClassResponse().apply {
                    roll = it.key
                    name = it.value[0].person
                    resultOfSubjects = it.value.associateBy({it.subject}, {
                        it.exam
                                .map { (it.percentile!! * it.obtainedNumber!!) / 100 }
                                .sum()
                    })
                } }

        return responseOK(result)
    }
}