package com.lynas.model

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship

/**
 * Created by LynAs on 8/18/2016
 */

@NodeEntity
data class Subject(
        @GraphId
        var id: Long? = null,
        var subjectName: String,
        var subjectDescription: String,
        var subjectBookAuthor: String,
        @Relationship(type = "curriculum", direction = Relationship.OUTGOING)
        var cls: Course)