package com.lynas.model

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship

/**
 * Created by sazzad on 8/9/16
 */
@NodeEntity
class Teacher {
    @GraphId
    var id: Long? = null

    var teacherID: String? = null
    var qualification: MutableList<Qualification>? = null
    var title: String? = null

    @Relationship(type = "teacherIsAPerson")
    var person: Person? = null
}