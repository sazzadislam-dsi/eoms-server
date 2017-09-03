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

    @Query("match (cl:Class)-[:classBelongsToAnOrganization]->(org:Organization) where ID(org) = {0} return cl")
    fun findListByOrganizationId(orgId: Long): List<Course>

    @Query("MATCH (cl:Class)-[r:classBelongsToAnOrganization]->(org:Organization)" +
            "where ID(cl) = {0} and ID(org) = {1} RETURN cl, r, org")
    fun findById(id:Long, orgId: Long): Course

    @Query("match (cl:Class{ name:{0},shift:{1}, section:{2}})-[:classBelongsToAnOrganization]->(org:Organization) where ID(org) = {3} return cl")
    fun findByProperty(className: String?, shift: String, section: String, orgId: Long): Course?

    @Query("match (p:Person) -[:studentIsAPerson]- (s:Student) -[e:Enrolment{year:{2}}]- (c:Class)," +
            "(c)-[:classBelongsToAnOrganization]->(org:Organization)" +
            "where ID(c) = {0} and ID(org) = {1}" +
            "return ID(s) as studentId, (p.firstName +' '+ p.lastName) as studentName, e.roleNumber as roleNumber, ID(e) as enrolmentId Order By e.roleNumber")
    fun findStudentsByClass(classId: Long?, orgId: Long, year: Int): Collection<ClassDetailQueryResult>

    @Query("match (p:Person) -[:studentIsAPerson]- (s:Student) -[e:Enrolment {year: {1}}]- (c:Class)" +
            "-[:classBelongsToAnOrganization]->(org:Organization)" +
            " where ID(c) = {0} and ID(org) = {2}" +
            "return ID(s) as studentId, (p.firstName +' '+ p.lastName) as studentName, e.roleNumber as roleNumber Order By e.roleNumber")
    fun findStudentsByClass(classId: Long?, year: Int?, orgId: Long): Collection<ClassDetailQueryResult>

    @Query("match (cl:Class)-[:classBelongsToAnOrganization]->(org:Organization) where ID(org) = {0} return count(cl)")
    fun findListCountByOrganizationName(orgId: Long): Int

    @Query("MATCH (n:Class{name:{0},shift:{1},section:{2}})-[:classBelongsToAnOrganization]->(org:Organization) where ID(org)={3} RETURN n LIMIT 1")
    fun findByPropAndOrg(name: String, shift: String, section: String, orgId: Long): Course?

}
