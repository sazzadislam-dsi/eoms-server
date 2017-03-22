package com.lynas.repo

import com.lynas.model.Enrolment
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository

/**
 * Created by sazzad on 7/18/16
 */

interface EnrolmentRepository : GraphRepository<Enrolment> {

    @Query("match (s:Student) -[e:Enrolment]- (c:Class) where ID(s) = {0}  return count(e)")
    fun countStudentEnrolment(studentId: Long): Int

}
