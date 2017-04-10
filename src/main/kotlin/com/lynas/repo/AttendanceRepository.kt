package com.lynas.repo

import com.lynas.model.AttendanceBook
import com.lynas.model.query.result.AttendanceViewQueryResult
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.stereotype.Repository

/**
 * Created by: seal on: 2/8/2017
 */
@Repository
interface AttendanceRepository : GraphRepository<AttendanceBook> {

    @Query("match (ab:AttendanceBook{attendanceDate:{0}})<-[:attendanceBookOfAClass]-(cl:Class{name:{1}})" +
            "-[:classBelongsToAnOrganization]->(org:Organization{name:{2}})," +
            " (ab)-[sa:studentsAttendance]->(sa1:StudentAttendance), (sa1)-[ofstudent:ofAStudent]->(student:Student)," +
            " (student)-[person:studentIsAPerson]-(p:Person), " +
            " (student) -[e:Enrolment]- (cl) " +
            " return ID(student) as studentId, (p.firstName +' '+ p.lastName) as studentName," +
            " e.roleNumber as roleNumber, sa1.attendanceStatus as isPresent Order By e.roleNumber")
    fun findAttendanceBookOfClass(date: Long, className: String, organizationName: String?): List<AttendanceViewQueryResult>
}