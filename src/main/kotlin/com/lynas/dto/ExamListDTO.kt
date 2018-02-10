package com.lynas.dto

data class ExamListDTO (val subjectId: Long,
                        val classId: Long,
                        val year: Int,
                        val listOfExamTypeDateDTO: List<ExamListQueryResult>
                        )
