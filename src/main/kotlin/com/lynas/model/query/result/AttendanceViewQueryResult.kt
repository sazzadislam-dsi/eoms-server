package com.lynas.model.query.result

import org.springframework.data.neo4j.annotation.QueryResult

/**
 * Created by seal on 4/11/2017.
 */
@QueryResult
class AttendanceViewQueryResult : ClassDetailQueryResult()  {
    var isPresent: Boolean? = null
}