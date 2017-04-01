package com.lynas.model.query.result

import org.springframework.data.neo4j.annotation.QueryResult

/**
 * Created by seal on 12/30/2016.
 */
@QueryResult
class ClassDetailQueryResult {
    var studentId: Long? = null
    var studentName: String? = null
    var roleNumber: Int? = null

}