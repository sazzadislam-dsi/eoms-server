package com.lynas.repo

import com.lynas.model.Exam
import com.lynas.model.query.result.ExamQueryResult
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository

/**
 * Created by sazzad on 7/18/16
 */

interface ExamRepository : GraphRepository<Exam> {

   @Query("match (s:Student) -[ e:Enrolment {year: {2}} ]- (cc:Class)," +
           " (cc)-[:classBelongsToAnOrganization]->(org:Organization)," +
           " (cc) <- [c:curriculum]- (sb:Subject) <- [ex:examOfSubject] -(exam:Exam{year: {2}})," +
           " (exam) -[info:examInfoOfPerson]-> (s) -[ss:studentIsAPerson]- (p:Person)" +
           "where ID(cc) = {0} and ID(sb) = {1} and ID(org) = {3} return collect(exam) as exam, sb.subjectName as subject, p.firstName as person, e.roleNumber as roleNumber, cc.name as courseName")
    fun resultOfSubjectByYear(classId: Long, subjectId: Long, year: Int, orgId: Long): List<ExamQueryResult>

    @Query("match (s:Student) -[ e:Enrolment {year: {2}} ]- (cc:Class)," +
           " (cc)-[:classBelongsToAnOrganization]->(org:Organization)," +
           " (cc) <- [c:curriculum]- (sb:Subject) <- [ex:examOfSubject] -(exam:Exam{year: {2}})," +
           " (exam) -[info:examInfoOfPerson]-> (s) -[ss:studentIsAPerson]- (p:Person)" +
           "where ID(cc) = {0} and ID(s) = {1} and ID(org) = {3} return collect(exam) as exam, sb.subjectName as subject, p.firstName as person, e.roleNumber as roleNumber, cc.name as courseName")
    fun resultOfStudentByYear(classId: Long, studentId: Long, year: Int, orgId: Long): List<ExamQueryResult>


    @Query("match (s:Student) -[ e:Enrolment {year: {1}} ]- (cc:Class)," +
           " (cc)-[:classBelongsToAnOrganization]->(org:Organization)," +
           " (cc) <- [c:curriculum]- (sb:Subject) <- [ex:examOfSubject] -(exam:Exam{year: {1}})," +
           " (exam) -[info:examInfoOfPerson]-> (s) -[ss:studentIsAPerson]- (p:Person)" +
           "where ID(cc) = {0} and ID(org) = {3} return collect(exam) as exam, sb.subjectName as subject, p.firstName as person, e.roleNumber as roleNumber, cc.name as courseName, ID(s) as studentId")
    fun resultOfClassByYear(classId: Long, year: Int, orgId: Long): List<ExamQueryResult>

}