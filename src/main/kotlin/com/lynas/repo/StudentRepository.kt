package com.lynas.repo

import com.lynas.dto.StudentInfoQueryResult
import com.lynas.model.Student
import org.springframework.data.neo4j.annotation.Depth
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository

/**
 * Created by sazzad on 7/18/16
 */

interface StudentRepository : GraphRepository<Student> {

    @Query("MATCH (s:Student)-[sp:studentIsAPerson]-> (p:Person)," +
            "(p)-[:personBelongsToAnOrganization]->(org:Organization)" +
            "WHERE ID(s) = {0} and ID(org) = {1}" +
            "return s, sp, p")
    @Depth(3)
    fun findOne(id: Long, orgId: Long): Student

    @Query("MATCH (s:Student) -[sp:studentIsAPerson]-> (p:Person)," +
            " (p)-[:personBelongsToAnOrganization]->(org:Organization) " +
            " where lower(p.firstName) contains lower ({0}) and ID(org) = {1} " +
            " return s, p, sp ")
    fun searchByFirstName(name: String, orgId: Long) : List<Student>

    @Query("match (s:Student) -[:studentIsAPerson]- (p:Person) <-[:personHasListOfContactInformation]- (c:ContactInformation)" +
            " (p)-[:personBelongsToAnOrganization]->(org:Organization) " +
            "where ID(c) = {0} and ID(org) = {1} return s")
    fun findStudentByContactId(id: Long, orgId : Long): Student

    @Query("match (s:Student) -[:studentIsAPerson]- (p:Person), (s) -[ e:Enrolment {year: {1}} ]- (cc:Class), " +
            "(p)-[:personBelongsToAnOrganization]->(org:Organization)" +
            "where ID(s) = {0}  and ID(org) = {2} " +
            "return ID(s) as studentId, ID(cc) as classID, p.firstName as firstName, p.lastName as lastName, cc.name as className, e.roleNumber as rollNumber , e.year as year")
    fun studentInfoByYear(id: Long, year: Int, orgId : Long): StudentInfoQueryResult

    @Query("MATCH (s:Student)-[sp:studentIsAPerson]-> (p:Person)," +
            "(p)-[:personBelongsToAnOrganization]->(org:Organization)" +
            " where ID(org) = {0} return s, sp, p")
    fun findAll(orgId : Long): Iterable<Student>

    @Query("MATCH (s:Student)-[sp:studentIsAPerson]-> (p:Person), (p)-[:personBelongsToAnOrganization]->(org:Organization) " +
            "WHERE ID(org) = {0} return count(s)")
    fun studentCountOfAnOrganization(orgId: Long): Int

}
