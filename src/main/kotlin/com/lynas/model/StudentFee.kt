package com.lynas.model

import org.neo4j.ogm.annotation.EndNode
import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.RelationshipEntity
import org.neo4j.ogm.annotation.StartNode
import org.neo4j.ogm.annotation.typeconversion.DateLong
import java.util.*

/**
 * Created by LynAs on 8/18/2016
 */

@RelationshipEntity(type = "StudentFee")
data class StudentFee(
        @GraphId
        var id: Long? = null,
        @DateLong
        var paymentDate: Date,
        @StartNode
        var student: Student,
        @EndNode
        var feeInfo: FeeInfo)