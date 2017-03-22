package com.lynas.model

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship
import org.neo4j.ogm.annotation.typeconversion.DateLong
import java.util.*

/**
 * Created by sazzad on 8/8/16
 */
@NodeEntity
class Student {
    @GraphId
    var id: Long? = null
    @Relationship(type = "studentIsAPerson")
    var person: Person? = null

    @DateLong
    val firstAdmissionDate: Date? = null

    override fun toString(): String {
        return "Student(person=$person, firstAdmissionDate=$firstAdmissionDate)"
    }


}