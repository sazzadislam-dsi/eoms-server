package com.lynas.service

import com.lynas.dto.*
import com.lynas.exception.EntityNotFoundException
import com.lynas.model.Exam
import com.lynas.model.Organization
import com.lynas.model.util.ExamType
import com.lynas.repo.ExamRepository
import com.lynas.util.convertToDate
import com.lynas.util.err_notFound
import org.neo4j.ogm.exception.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalStateException
import java.util.*

/**
 * Created by sazzad on 7/19/16
 */

@Service
class ExamService(private val examRepository: ExamRepository,
                  private val classService: ClassService,
                  private val subjectService: SubjectService,
                  private val studentService: StudentService) {

    @Transactional
    fun create(examJson: ExamJsonWrapper, organization: Organization) {
        val _date: Date = examJson.date.convertToDate()

        val course = classService.findById(examJson.classId, organization.id!!)
                ?: throw EntityNotFoundException("ClassId/CourseId ${examJson.classId}".err_notFound())
        val _subject = subjectService.findById(examJson.subjectId)
        val listOfExam = examJson.examDTO.map { (mark, studentId, _isPresent) ->
            Exam(
                    examType = examJson.examType,
                    totalNumber = examJson.totalMark,
                    percentile = examJson.percentile,
                    isPresent = _isPresent,
                    date = _date,
                    year = examJson.year,
                    cls = course,
                    subject = _subject,
                    obtainedNumber = mark,
                    student = studentService.findById(studentId, organization.id!!)
                            ?: throw EntityNotFoundException("student not found with studentID: $studentId")
            )
        }
        examRepository.save(listOfExam)
    }

    @Transactional
    fun create(exam: Exam) {
        examRepository.save(exam)
    }

    @Transactional
    fun findById(examId: Long): Exam {
        return examRepository.findOne(examId) ?: throw EntityNotFoundException("Exam not found with id: $examId")
    }

    @Transactional
    fun update(examUpdateDTO: ExamUpdateDTO) {
        val exam = findById(examUpdateDTO.examId)
        if (exam.totalNumber < examUpdateDTO.updateObtainMark)
            throw IllegalStateException("updated mark : ${examUpdateDTO.updateObtainMark} is grater then " +
                    "total mark ${exam.totalNumber} of exam")
        exam.obtainedNumber = examUpdateDTO.updateObtainMark
        create(exam)
    }

    @Transactional
    fun resultOfSubjectByYear(classId: Long, subjectId: Long, _year: Int, orgId: Long): ExamResponse {
        val resultList = examRepository.resultOfSubjectByYear(classId, subjectId, _year, orgId)
        val subject = subjectService.findById(subjectId)
        val examResponse = ExamResponse().apply {
            className = classService.findById(classId, orgId)?.name
            subjectName = subject.subjectName
            year = _year
        }

        val studentRollNumberToResultMap = resultList.associateBy(
                { it.roleNumber }, { it.exam.map { (it.percentile * it.obtainedNumber) / 100 }.sum() })

        resultList.forEach {
            val student = ExamResponse.Student().apply {
                name = it.person
                rollNumber = it.roleNumber
            }

            student.exams = it.exam.map {
                ExamResponse.Exam().apply {
                    examId = it.id
                    examType = it.examType
                    totalMark = it.totalNumber
                    obtainMark = it.obtainedNumber
                }
            }.toMutableList()
            //TODO result property name confusing
            student.result = studentRollNumberToResultMap[student.rollNumber]
            examResponse.student.add(student)
        }

        examResponse.student.sortBy { it.rollNumber }

        return examResponse
    }

    @Transactional
    fun resultOfStudentByYear(classId: Long, studentId: Long, _year: Int, orgId: Long): ExamStudentResponse {
        val resultList = examRepository.resultOfStudentByYear(classId, studentId, _year, orgId)
        val studentInfo = studentService.studentInfoByYear(studentId, _year, orgId)
        val response = ExamStudentResponse().apply {
            studentName = studentInfo.firstName
            className = studentInfo.firstName
            rollNumber = studentInfo.rollNumber
            year = studentInfo.year
        }

        response.examBySubject = resultList.associateBy({ it.subject }, {
            it.exam.map {
                ExamStudentResponse.Exam().apply {
                    examType = it.examType
                    totalMark = it.totalNumber
                    obtainMark = it.obtainedNumber
                }
            }.toMutableList()
        })

        response.resultBySubject = resultList.associateBy(
                { it.subject }, { it.exam.map { (it.percentile * it.obtainedNumber) / 100 }.sum() })
        return response
    }

    @Transactional
    fun resultOfClass(classId: Long, _year: Int, orgId: Long): List<ExamQueryResult>
            = examRepository.resultOfClassByYear(classId, _year, orgId)

    @Transactional
    fun examListOfSubject(classId: Long, subjectId: Long, _year: Int, orgId: Long): ExamListDTO {
        return ExamListDTO(classId = classId,
                subjectId = subjectId,
                year = _year,
                listOfExamTypeDateDTO = examRepository.examListBySubject(classId, subjectId, _year, orgId)
        )
    }

    @Transactional
    fun findByClassIdSubjectIdYearDateExamType(classId: Long, subjectId: Long, year: Int, date: Date,
                                               examType: ExamType, orgId: Long): ExamOfSubjectUpdateDTO {
        val examOfSubjectUpdateDTO = ExamOfSubjectUpdateDTO()
        examOfSubjectUpdateDTO.classId = classService.findById(classId, orgId).id!!
        examOfSubjectUpdateDTO.subjectId = subjectService.findById(subjectId).id!!
        examOfSubjectUpdateDTO.examType = examType
        examOfSubjectUpdateDTO.date = date

        // TODO compare with date also , by now date is ignored
        val results = examRepository.findByExamTypeAndDate(classId, subjectId, year, orgId, examType)
        if (results.isEmpty()) {
            throw NotFoundException("classId : [$classId], subjectId : [$subjectId], examType : [$examType]")
        }

        examOfSubjectUpdateDTO.percentile = results[0].percentile!!
        examOfSubjectUpdateDTO.totalNumber = results[0].totalNumber!!

        examOfSubjectUpdateDTO.resultOfASubjectByExamTypeDate = results
                .map {
                    val exam = it.exam[0]
                    ExamOfStudent(studentId = exam.student.id!!,
                            name = it.person!!,
                            rollNumber = it.roleNumber!!,
                            examId = exam.id!!,
                            obtainMark = exam.obtainedNumber,
                            p = exam.isPresent)
                }
                .sortedBy { it.rollNumber }
                .toMutableList()

        return examOfSubjectUpdateDTO
    }
}