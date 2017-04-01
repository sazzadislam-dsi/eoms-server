package com.lynas.controller.rest

import com.lynas.model.Exam
import com.lynas.model.request.ExamJsonWrapper
import com.lynas.model.response.ExamResponse
import com.lynas.service.ClassService
import com.lynas.service.ExamService
import com.lynas.service.StudentService
import com.lynas.service.SubjectService
import com.lynas.util.getLogger
import com.lynas.util.responseOK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
    fun post(@RequestBody examJson: ExamJsonWrapper): ResponseEntity<*> {
        logger.info("hit in post controller with {}", examJson)
        val course = classService.findById(examJson.classId)
        val _subject = subjectService.findById(examJson.subjectId)
        val listOfExam = examJson.examJson.map {
            i ->
            Exam().apply {
                examType = examJson.examType
                totalNumber = examJson.totalMark
                percentile = examJson.percentile
                year = examJson.year
                cls = course
                subject = _subject
                obtainedNumber = i.mark
                student = studentService.findById(i.studentId)
            }
        }
        examService.save(listOfExam)
        return responseOK(examJson)
    }

    @GetMapping("/class/{classId}/subject/{subjectId}/year/{_year}/results")
    fun resultOfClassBySubject(@PathVariable classId: Long,
                               @PathVariable subjectId: Long,
                               @PathVariable _year: Int): ResponseEntity<*> {
        logger.info("return result of class id {} and subject id {} and year {}", classId, subjectId, _year)
        return responseOK(examService.resultOfSubjectByYear(classId, subjectId, _year))
    }

    @GetMapping("/class/{classId}/student/{studentId}/year/{_year}/results")
    fun resultOfStudentByYear(@PathVariable classId: Long,
                               @PathVariable studentId: Long,
                               @PathVariable _year: Int): ResponseEntity<*> {
        logger.info("return result of class id {} and student id {} and year {}", classId, studentId, _year)
        return responseOK(examService.resultOfStudentByYear(classId, studentId, _year))
    }
}