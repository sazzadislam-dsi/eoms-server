package com.lynas.service

import com.lynas.model.Exam
import com.lynas.model.query.result.ExamQueryResult
import com.lynas.model.response.ExamResponse
import com.lynas.model.response.ExamStudentResponse
import com.lynas.repo.ExamRepository
import com.lynas.service.dto.ExamListDTO
import org.neo4j.ogm.exception.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by sazzad on 7/19/16
 */

@Service
class ExamService(private val examRepository: ExamRepository,
                  private val classService: ClassService,
                  private val subjectService: SubjectService,
                  private val studentService: StudentService) {

    @Transactional
    fun create(exam: Collection<Exam>) {
        examRepository.save(exam)
    }

    @Transactional
    @Throws(NotFoundException::class)
    fun resultOfSubjectByYear(classId: Long, subjectId: Long, _year: Int, orgId: Long): ExamResponse {
        val resultList = examRepository.resultOfSubjectByYear(classId, subjectId, _year, orgId)
        val subject = subjectService.findById(subjectId) ?: throw NotFoundException("subjectId : $subjectId")
        val examResponse = ExamResponse().apply {
            className = classService.findById(classId, orgId)?.name
            subjectName = subject.subjectName
            year = _year
        }

        val studentRollNumberToResultMap = resultList.associateBy(
                { it.roleNumber }, { it.exam.map { (it.percentile!! * it.obtainedNumber!!) / 100 }.sum() })

        resultList.forEach {
            val student = ExamResponse.Student().apply {
                name = it.person
                rollNumber = it.roleNumber
            }

            student.exams = it.exam.map {
                ExamResponse.Exam().apply {
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
                { it.subject },{it.exam.map { (it.percentile!! * it.obtainedNumber!!) / 100 }.sum()})
        return response
    }

    @Transactional
    fun resultOfClass(classId: Long, _year: Int, orgId: Long): List<ExamQueryResult> {
        val resultList = examRepository.resultOfClassByYear(classId, _year, orgId)
        val resultMap = resultList.groupBy { it.roleNumber }
        return resultList
    }

    @Transactional
    fun examListOfSubject(classId: Long, subjectId: Long, _year: Int, orgId: Long): ExamListDTO {
        return ExamListDTO(classId = classId,
                subjectId = subjectId,
                year = _year,
                listOfExamTypeDateDTO = examRepository.examListBySubject(classId, subjectId, _year, orgId)
        )
    }
}