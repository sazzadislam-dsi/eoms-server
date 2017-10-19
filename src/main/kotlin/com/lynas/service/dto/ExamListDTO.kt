package com.lynas.service.dto

import com.lynas.model.util.ExamListQueryResult

data class ExamListDTO (val subjectId: Long,
                        val classId: Long,
                        val year: Int,
                        val listOfExamTypeDateDTO: List<ExamListQueryResult>
                        )
