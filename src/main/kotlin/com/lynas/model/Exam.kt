package com.lynas.model

import com.lynas.model.util.ExamType
import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship

/**
 * Created by sazzad on 8/31/16
 */
@NodeEntity
class Exam {
    @GraphId
    var id: Long? = null
    var examType: ExamType? = null
    var totalNumber: Long? = null
    var obtainedNumber: Long? = null

    @Relationship(type = "examOfClass", direction = Relationship.OUTGOING)
    var cls: Course? = null
    @Relationship(type = "examOfSubject", direction = Relationship.OUTGOING)
    var subject: Subject? = null
    @Relationship(type = "examInfoOfPerson", direction = Relationship.OUTGOING)
    var student: Student? = null

}