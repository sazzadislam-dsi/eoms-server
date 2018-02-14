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

    @Query("""match (sub:Subject) -[c:curriculum]-> (cls:Class),
        (cls)-[:classBelongsToAnOrganization]->(org:Organization) where ID(cls) = {0}  and ID(org) = {1} return sub""")
    fun findAllByClassId(classId: Long, orgId: Long): List<Subject>

    @Query("""match (s:Student) -[e:Enrolment]- (cc:Class),
        (cc)-[:classBelongsToAnOrganization]->(org:Organization), (cc) <-[c:curriculum]- (sb:Subject)
         where ID(s) = {0} and ID(org) = {1} return sb""")
    fun findAllByStudentId(stdId: Long, orgId: Long): List<Subject>
}