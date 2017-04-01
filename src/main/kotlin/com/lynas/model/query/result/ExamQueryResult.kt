package com.lynas.model.query.result

import com.lynas.model.Exam
import org.springframework.data.neo4j.annotation.QueryResult

/**
 * Created by muztaba.hasanat on 2/26/2017.
 */
@QueryResult
data class ExamQueryResult (
        var courseName: String? = null,
        var exam: List<Exam> = mutableListOf(),
        var subject: String? = null,
        var person: String? = null,
        var roleNumber: Int? = null
)