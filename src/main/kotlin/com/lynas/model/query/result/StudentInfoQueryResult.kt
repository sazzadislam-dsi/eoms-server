package com.lynas.model.query.result

import org.springframework.data.neo4j.annotation.QueryResult

/**
 * Created by seal on 3/19/2017.
 */
@QueryResult
data class StudentInfoQueryResult(
        var studentId: Long = 0,
        var classId: Long = 0,
        var firstName: String = "",
        var lastName: String = "",
        var className: String = "",
        var rollNumber: Int = 0,
        var year: Int = 0
)