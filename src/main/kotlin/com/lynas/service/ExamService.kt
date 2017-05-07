package com.lynas.service

import com.lynas.model.Exam
import com.lynas.model.query.result.ExamQueryResult
import com.lynas.model.response.ExamResponse
import com.lynas.model.response.ExamStudentResponse
import com.lynas.repo.ExamRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by sazzad on 7/19/16
 */

@Service
open class ExamService(val examRepository: ExamRepository,
                       val classService: ClassService,
                       val subjectService: SubjectService,
                       val studentService: StudentService) {

    @Transactional
    open fun save(exam: Collection<Exam>) {
        examRepository.save(exam)
    }

    @Transactional
    open fun resultOfSubjectByYear(classId: Long, subjectId: Long, _year: Int, organization: String): ExamResponse {
        val resultList = examRepository.resultOfSubjectByYear(classId, subjectId, _year, organization)
        val examResponse = ExamResponse().apply {
            className = classService.findById(classId).name
            subjectName = subjectService.findById(subjectId).subjectName
            year = _year
        }

        val map = resultList.associateBy({ it.roleNumber },
                {
                    it.exam
                            .map { (it.percentile!! * it.obtainedNumber!!) / 100 }
                            .sum()
                })

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
            student.result = map[student.rollNumber]
            examResponse.student.add(student)
        }

        examResponse.student.sortBy { it.rollNumber }

        return examResponse
    }

    @Transactional
    open fun resultOfStudentByYear(classId: Long, studentId: Long, _year: Int, organization: String): ExamStudentResponse {
        val resultList = examRepository.resultOfStudentByYear(classId, studentId, _year, organization)
        val studentInfo = studentService.studentInfoByYear(studentId, _year, organization)
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

        response.resultBySubject = resultList.associateBy({ it.subject },
                {
                    it.exam
                            .map { (it.percentile!! * it.obtainedNumber!!) / 100 }
                            .sum()
                })

        return response
    }

    @Transactional
    open fun resultOfClass(classId: Long, _year: Int, organization: String): List<ExamQueryResult> {
        val resultList = examRepository.resultOfClassByYear(classId, _year, organization)
        val resultMap = resultList.groupBy { it.roleNumber }

       return resultList

    }
}