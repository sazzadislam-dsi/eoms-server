package com.lynas.model

import org.neo4j.ogm.annotation.EndNode
import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.RelationshipEntity
import org.neo4j.ogm.annotation.StartNode
import java.util.*

/**
 * Created by LynAs on 8/18/2016
 */

@RelationshipEntity(type = "StudentFee")
class StudentFee {

    @GraphId
    var id: Long? = null
    var date: Date? = null
    @StartNode
    var student: Student? = null
    @EndNode
    var feeInfo: FeeInfo? = null

}