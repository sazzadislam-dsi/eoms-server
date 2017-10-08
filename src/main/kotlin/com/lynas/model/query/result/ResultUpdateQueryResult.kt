package com.lynas.model.query.result

import com.lynas.model.Exam
import org.springframework.data.neo4j.annotation.QueryResult

@QueryResult
data class ResultUpdateQueryResult (
        var courseName: String? = null,
        var exam: List<Exam> = mutableListOf(),
        var subject: String? = null,
        var person: String? = null,
        var roleNumber: Int? = null,
        var studentId: Long? = null,
        var percentile: Double? = null,
        var totalNumber: Double? = null
)