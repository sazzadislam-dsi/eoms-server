package com.lynas.model

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship
import org.neo4j.ogm.annotation.typeconversion.DateLong
import java.util.*

/**
 * Created by LynAs on 7/23/2016
 */
/**
 * domain must be normal class coz it needs no arg constructor and field
 * needs to be var so that it can be re assign by neo4j ogm
 */

@NodeEntity
class FeeInfo {
    @GraphId
    var id: Long? = null
    var type: String? = null
    var amount: Double? = null
    var year: Int? = null
    @DateLong
    var lastDate: Date? = null
    @DateLong
    var dateCreated: Date = Date()
    @DateLong
    var dateModified: Date = Date()

    @Relationship(type = "feeInfoOfCourse", direction = Relationship.OUTGOING)
    var course: Course? = null
}
