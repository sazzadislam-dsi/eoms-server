package com.lynas.model

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship

/**
 * Created by LynAs on 8/18/2016
 */

@NodeEntity
data class StudentAttendance(
        @GraphId
        var id: Long? = null,
        @Relationship(type = "ofAStudent", direction = Relationship.OUTGOING)
        var student: Student,
        var attendanceStatus: Boolean)