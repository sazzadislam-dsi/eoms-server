package com.lynas.repo

import com.lynas.model.Subject
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.stereotype.Repository

/**
 * Created by seal on 2/6/2017.
 */
@Repository
interface SubjectRepository : GraphRepository<Subject> {

    @Query("match (s:Subject) <- [c:curriculum] - (cls:Class)," +
            "(cls)-[:classBelongsToAnOrganization]->(org:Organization {name: {1}})" +
            "where ID(cls) = {0} return s")
    fun findAllByClassId(classId: Long, organization: String): List<Subject>

    @Query("match (s:Student) -[e:Enrolment]- (cc:Class)," +
            "(cc)-[:classBelongsToAnOrganization]->(org:Organization {name: {1}})," +
            "(cc)-[c:curriculum]-> (sb:Subject)" +
            "where ID(s) = {0} return sb")
    fun findAllByStudentId(stdId: Long, organization: String): List<Subject>
}