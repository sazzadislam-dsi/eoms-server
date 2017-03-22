package com.lynas.model

import org.neo4j.ogm.annotation.EndNode
import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.RelationshipEntity
import org.neo4j.ogm.annotation.StartNode

/**
 * Created by LynAs on 8/18/2016
 */

@RelationshipEntity(type = "Enrolment")
class Enrolment {
    @GraphId
    var id: Long? = null
    var year: Int? = null
    var roleNumber: Int? = null
    @StartNode
    var student: Student? = null
    @EndNode
    var cls: Course? = null
}