package com.lynas.model

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship
import org.neo4j.ogm.annotation.typeconversion.DateLong
import java.util.*

/**
 * Created by LynAs on 7/23/2016
 */

@NodeEntity
data class FeeInfo(
        @GraphId
        var id: Long? = null,
        var type: String,
        var amount: Double,
        var year: Int,
        @DateLong
        var lastDate: Date,
        @DateLong
        var dateCreated: Date,
        @DateLong
        var dateModified: Date,

        @Relationship(type = "feeInfoOfCourse", direction = Relationship.OUTGOING)
        var course: Course)