package com.lynas.model

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship
import org.neo4j.ogm.annotation.typeconversion.DateLong
import java.util.*

/**
 * Created by sazzad on 8/22/16
 */

@NodeEntity
class AttendanceBook {
    @GraphId
    var id: Long? = null
    @Relationship(type = "attendanceBookOfAClass", direction = Relationship.INCOMING)
    var course: Course? = null
    @DateLong
    var attendanceDate: Date? = null
    var studentAttendances: MutableSet<StudentAttendance> = mutableSetOf()
}
