package com.lynas.repo

import com.lynas.model.AttendanceBook
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.stereotype.Repository

/**
 * Created by: seal on: 2/8/2017
 */
@Repository
interface AttendanceRepository : GraphRepository<AttendanceBook> {

    @Query("match (ab:AttendanceBook{attendanceDate:{0}})<-[:attendanceBookOfAClass]-(cl:Class{name:{1}})" +
            "-[:classBelongsToAnOrganization]->(org:Organization{name:{2}}) return ab,cl")
    fun findAttendanceBookOfClass(date: Long, className: String, organizationName: String?): List<AttendanceBook>
}