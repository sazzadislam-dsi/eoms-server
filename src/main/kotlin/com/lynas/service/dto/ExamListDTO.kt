package com.lynas.service.dto

import com.lynas.model.query.result.ExamListQueryResult

data class ExamListDTO (val subjectId: Long,
                        val classId: Long,
                        val year: Int,
                        val listOfExamTypeDateDTO: List<ExamListQueryResult>
                        )
