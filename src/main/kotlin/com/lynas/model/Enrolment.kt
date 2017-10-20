package com.lynas.model

import org.neo4j.ogm.annotation.EndNode
import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.RelationshipEntity
import org.neo4j.ogm.annotation.StartNode

/**
 * Created by LynAs on 8/18/2016
 */

@RelationshipEntity(type = "Enrolment")
class Enrolment(
        @GraphId
        var id: Long? = null,
        var year: Int,
        var roleNumber: Int,
        @StartNode
        var student: Student,
        @EndNode
        var cls: Course) {
    constructor() : this(null, 0, 0, Student(), Course())
}