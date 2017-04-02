package com.lynas.model

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity

/**
 * Created by LynAs on 8/18/2016
 */

@NodeEntity
class StudentAttendance {

    @GraphId
    var id: Long? = null
    var student: Student? = null
    var attendanceStatus: Boolean? = null
}
