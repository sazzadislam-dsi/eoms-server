package com.lynas.repo

import com.lynas.model.Enrolment
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository

/**
 * Created by sazzad on 7/18/16
 */

interface EnrolmentRepository : GraphRepository<Enrolment> {

    @Query("""match (s:Student) -[e:Enrolment {year: {1}}]-> (c:Class),
            (c)-[:classBelongsToAnOrganization]->(org:Organization)
            where ID(s) = {0} and ID(org) = {2}  return e""")
    fun findEnrollmentOfStudentByYear(studentId: Long, year: Int, orgId: Long): Enrolment?


    @Query("""match (s:Student) -[e:Enrolment {year: {1}, roleNumber:{0}}]-> (c:Class),
            (c)-[:classBelongsToAnOrganization]->(org:Organization)
            where ID(org) = {2} and ID(c) = {3}  return e""")
    fun findEnrollmentOfRole(roleNumber: Int, year: Int, orgId: Long, classId: Long): List<Enrolment>

}
