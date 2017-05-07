package com.lynas.repo

import com.lynas.model.Student
import com.lynas.model.query.result.StudentInfoQueryResult
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository

/**
 * Created by sazzad on 7/18/16
 */

interface StudentRepository : GraphRepository<Student> {

    @Query("MATCH (s:Student)-[sp:studentIsAPerson]-> (p:Person)," +
            "(p)-[:personBelongsToAnOrganization]->(org:Organization {name:{1}})" +
            "WHERE ID(s) = {0}" +
            "return s, sp, p")
    fun findOne(id: Long, organization: String): Student

    @Query("MATCH (s:Student) -[sp:studentIsAPerson]-> (p:Person {firstName:{0}})," +
            "(p)-[:personBelongsToAnOrganization]->(org:Organization {name:{1}})" +
            "return s, p, sp")
    fun searchByFirstName(name: String, organization: String) : List<Student>

    @Query("match (s:Student) -[:studentIsAPerson]- (p:Person) <-[:personHasListOfContactInformation]- (c:ContactInformation)" +
            "(p)-[:personBelongsToAnOrganization]->(org:Organization {name:{1}})" +
            "where ID(c) = {0} return s")
    fun findStudentByContactId(id: Long, organization: String): Student

    @Query("match (s:Student) -[:studentIsAPerson]- (p:Person), (s) -[ e:Enrolment {year: {1}} ]- (cc:Class), " +
            "(p)-[:personBelongsToAnOrganization]->(org:Organization {name:{2}})" +
            "where ID(s) = {0} " +
            "return ID(s) as studentId, p.firstName as firstName, p.lastName as lastName, cc.name as className, e.roleNumber as rollNumber , e.year as year")
    fun studentInfoByYear(id: Long, year: Int, organization: String): StudentInfoQueryResult

    @Query("MATCH (s:Student)-[sp:studentIsAPerson]-> (p:Person)," +
            "(p)-[:personBelongsToAnOrganization]->(org:Organization {name:{0}})" +
            "return s, sp, p")
    fun findAll(organization: String): Iterable<Student>

}
