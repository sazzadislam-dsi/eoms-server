package com.lynas.controller.rest

import com.lynas.model.Exam
import com.lynas.model.response.ErrorObject
import com.lynas.model.response.ExamClassResponse
import com.lynas.model.util.ExamJsonWrapper
import com.lynas.model.util.ExamUpdateJson
import com.lynas.service.*
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
class ExamRestController(val examService: ExamService,
                         val classService: ClassService,
                         val subjectService: SubjectService,
                         val studentService: StudentService,
                         val examServiceJava: ExamServiceJava) {

    val logger = getLogger(this.javaClass)

    @PostMapping
    fun post(@RequestBody examJson: ExamJsonWrapper, request: HttpServletRequest): ResponseEntity<*> {
        logger.info("hit in create controller with {}", examJson)
        val organization = getOrganizationFromSession(request)
        val _date: Date = examJson.date.convertToDate()
        val course = classService.findById(examJson.classId, organization.id!!)
                ?: return responseError("ClassId/CourseId ${examJson.classId}".err_notFound())
        val _subject = subjectService.findById(examJson.subjectId)
                ?: return responseError("SubjectId ${examJson.subjectId}".err_notFound())
        val listOfExam = examJson.examJson.map { (mark, studentId, _isPresent) ->
            Exam().apply {
                examType = examJson.examType
                totalNumber = examJson.totalMark
                percentile = examJson.percentile
                isPresent = _isPresent
                date = _date
                year = examJson.year
                cls = course
                subject = _subject
                obtainedNumber = mark
                student = studentService.findById(studentId, organization.id!!)
                        ?: return responseError(ErrorObject(
                        examJson,
                        "subjectId",
                        "student not found with studentID: $studentId",
                        ""))
            }
        }
        examService.create(listOfExam)
        return responseOK(examJson)
    }

    @PatchMapping
    fun updateExamMark(@RequestBody examUpdateJson: ExamUpdateJson, request: HttpServletRequest): ResponseEntity<*> {
        logger.info("Update examId [{}], with updated obtain mark [{}]", examUpdateJson.examId, examUpdateJson.updateObtainMark)
        return try {
            examService.update(examUpdateJson)
            responseOK(examUpdateJson)
        } catch (e: NotFoundException) {
            responseError(e.message ?: "")
        } catch (e: Exception) {
            responseError(e.message
                    ?: "(ExamRestController:resultOfClassBySubject:Exception) Error check server")
        }
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
                    orgId = getCurrentUserOrganizationId(request)))
        } catch (e: NotFoundException) {
            responseError(e.message
                    ?: "(ExamRestController:resultOfClassBySubject:NotFoundException) Error check server")
        } catch (e: Exception) {
            responseError(e.message
                    ?: "(ExamRestController:resultOfClassBySubject:Exception) Error check server")
        }
    }

    @GetMapping("student/{studentId}/results")
    fun resultOfStudentByYear(@PathVariable studentId: Long, request: HttpServletRequest): ResponseEntity<*> {
        logger.info("return result for student id [{}]", studentId)
        val organization = getOrganizationFromSession(request)
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
        // TODO why following code was written
        val result = examService.resultOfClass(classId, _year, organization.id!!).groupBy { it.roleNumber }
                .map {
                    ExamClassResponse().apply {
                        roll = it.key
                        name = it.value[0].person
                        resultOfSubjects = it.value.associateBy({ it.subject }, {
                            it.exam
                                    .map { (it.percentile!! * it.obtainedNumber!!) / 100 }
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
        val organization = getOrganizationFromSession(request)
        logger.info("Hit method with classId [{}], year [{}], organization [{}]", classId, _year, organization.id)
        val result = examServiceJava.getResultOfClass(classId, _year, organization.id!!)
        return responseOK(result)
    }
}