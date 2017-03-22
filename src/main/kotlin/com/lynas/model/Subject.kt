package com.lynas.model

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship

/**
 * Created by LynAs on 8/18/2016
 */

@NodeEntity
class Subject {
    @GraphId
    var id: Long? = null
    var subjectName: String? = null
    var subjectDescription: String? = null
    var subjectBookAuthor: String? = null

    @Relationship(type = "subjectOfAClass", direction = Relationship.OUTGOING)
    var course: Course? = null
}