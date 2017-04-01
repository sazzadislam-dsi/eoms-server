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

    @Query("match (s:Subject) <- [c:curriculum] - (cls:Class) where ID(cls) = {0} return s")
    fun findAllByClassId(classId: Long): List<Subject>

    @Query("match (s:Student) -[e:Enrolment]- (cc:Class) - [c:curriculum]-> (sb:Subject) where ID(s) = {0} return sb")
    fun findAllByStudentId(stdId: Long): List<Subject>
}