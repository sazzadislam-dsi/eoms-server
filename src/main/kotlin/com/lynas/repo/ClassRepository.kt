package com.lynas.repo

import com.lynas.model.Course
import com.lynas.model.query.result.ClassDetailQueryResult
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository

/**
 * Created by sazzad on 7/18/16
 */

interface ClassRepository : GraphRepository<Course> {
    fun findListByName(name: String): List<Course>

    @Query("match (cl:Class)-[:classBelongsToAnOrganization]->(org:Organization {name:{0}} ) return cl")
    fun findListByOrganizationName(name: String?): List<Course>

    @Query("MATCH (s) WHERE ID(s) = {0} RETURN s")
    fun findById(id:Long): Course

    @Query("match (cl:Class{ name:{0},shift:{1}, section:{2}})-[:classBelongsToAnOrganization]->(org:Organization {name:{3}} ) return cl")
    fun findByProperty(className: String?, shift: String, section: String, orgName: String?): Course?

    @Query("match (p:Person) -[:studentIsAPerson]- (s:Student) -[e:Enrolment]- (c:Class) where ID(c) = {0}   " +
            "return ID(s) as studentId, p.firstName as studentName, e.roleNumber as roleNumber Order By e.roleNumber")
    fun findStudentsByClass(classId: Long?): Collection<ClassDetailQueryResult>

}
