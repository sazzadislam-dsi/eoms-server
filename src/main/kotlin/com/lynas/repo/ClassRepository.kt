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

    @Query("MATCH (cl:Class)-[:classBelongsToAnOrganization]->(org:Organization {name:{1}} )" +
            "where ID(cl) = {0} RETURN cl")
    fun findById(id: Long, organization: String): Course

    @Query("match (cl:Class{ name:{0},shift:{1}, section:{2}})-[:classBelongsToAnOrganization]->(org:Organization {name:{3}} ) return cl")
    fun findByProperty(className: String?, shift: String, section: String, orgName: String?): Course?

    @Query("match (p:Person) -[:studentIsAPerson]- (s:Student) -[e:Enrolment{year:{2}}]- (c:Class)," +
            "(c)-[:classBelongsToAnOrganization]->(org:Organization {name:{1}})" +
            "where ID(c) = {0}" +
            "return ID(s) as studentId, (p.firstName +' '+ p.lastName) as studentName, e.roleNumber as roleNumber Order By e.roleNumber")
    fun findStudentsByClass(classId: Long?, organization: String, year: Int): Collection<ClassDetailQueryResult>

    @Query("match (p:Person) -[:studentIsAPerson]- (s:Student) -[e:Enrolment {year: {1}}]- (c:Class)" +
            "-[:classBelongsToAnOrganization]->(org:Organization {name:{2}})" +
            " where ID(c) = {0}" +
            "return ID(s) as studentId, (p.firstName +' '+ p.lastName) as studentName, e.roleNumber as roleNumber Order By e.roleNumber")
    fun findStudentsByClass(classId: Long?, year: Int?, organization: String): Collection<ClassDetailQueryResult>

    @Query("match (cl:Class)-[:classBelongsToAnOrganization]->(org:Organization {name:{0}} ) return count(cl)")
    fun findListCountByOrganizationName(name: String?): Int

    @Query("MATCH (n:Class{name:{0},shift:{1},section:{2}})-[:classBelongsToAnOrganization]->(org:Organization) where ID(org)={3} RETURN n LIMIT 1")
    fun findByPropAndOrg(name: String, shift: String, section: String, orgId: Long): Course?

}
