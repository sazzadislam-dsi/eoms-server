package com.lynas.model.query.result

import com.lynas.model.util.ExamType
import org.springframework.data.neo4j.annotation.QueryResult
import java.util.*

@QueryResult
open class ExamListQueryResult {
    var date: Date = Date()
    var examType: ExamType = ExamType.FINAL
}