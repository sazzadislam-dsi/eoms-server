package com.lynas.model

import com.lynas.model.util.ExamType
import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship
import java.util.*

/**
 * Created by sazzad on 8/31/16
 */
@NodeEntity
data class Exam (
    @GraphId
    var id: Long? = null,
    var examType: ExamType,
    var year: Int,
    var totalNumber: Double,
    var obtainedNumber: Double,
    var percentile: Double,
    var isPresent: Boolean,
    var date: Date,

    @Relationship(type = "examOfClass", direction = Relationship.OUTGOING)
    var cls: Course,
    @Relationship(type = "examOfSubject", direction = Relationship.OUTGOING)
    var subject: Subject,
    @Relationship(type = "examInfoOfPerson", direction = Relationship.OUTGOING)
    var student: Student){
    constructor() : this(null, ExamType.FIRST_TERM, 0, 0.0, 0.0, 100.0, true, Date(), Course(), Subject(), Student())
}