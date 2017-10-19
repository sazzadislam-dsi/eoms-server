package com.lynas.repo

import com.lynas.model.Exam
import com.lynas.model.util.ExamListQueryResult
import com.lynas.model.util.ExamQueryResult
import com.lynas.model.util.ExamType
import com.lynas.model.util.ResultUpdateQueryResult
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
           "where ID(cc) = {0} and ID(org) = {2} return collect(exam) as exam, sb.subjectName as subject, p.firstName as person, e.roleNumber as roleNumber, cc.name as courseName, ID(s) as studentId")
    fun resultOfClassByYear(classId: Long, year: Int, orgId: Long): List<ExamQueryResult>

    @Query(" MATCH (cc)-[:classBelongsToAnOrganization]->(org:Organization), " +
            " (cc:Class) <- [c:curriculum]- (sb:Subject) <- [ex:examOfSubject] -(exam:Exam{year: {2}}) " +
            " where ID(cc) = {0} and ID(sb) = {1} and ID(org) = {3} return distinct  exam.examType ")
    fun takenExamsByClassAndSubject(classId: Long, subjectId: Long, year: Int, orgId: Long): List<ExamType>

    @Query(" MATCH (cc)-[:classBelongsToAnOrganization]->(org:Organization), " +
            " (cc:Class) <- [c:curriculum]- (sb:Subject) <- [ex:examOfSubject] -(exam:Exam{year: {2}}) " +
            " where ID(cc) = {0} and ID(sb) = {1} and ID(org) = {3} return  distinct exam.date as date, exam.examType as examType ")
    fun examListBySubject(classId: Long, subjectId: Long, year: Int, orgId: Long) : List<ExamListQueryResult>

 @Query("match (s:Student) -[ e:Enrolment {year: {2}} ]- (cc:Class)," +
         " (cc)-[:classBelongsToAnOrganization]->(org:Organization)," +
         " (cc) <- [c:curriculum]- (sb:Subject) <- [ex:examOfSubject] -(exam:Exam{year: {2}, examType: {4}})," +
         " (exam) -[info:examInfoOfPerson]-> (s) -[ss:studentIsAPerson]- (p:Person)" +
         " where ID(cc) = {0} and ID(sb) = {1} and ID(org) = {3} return collect(exam) as exam, sb.subjectName as subject, " +
         " p.firstName as person, e.roleNumber as roleNumber, cc.name as courseName, info,  s, exam.totalNumber as totalNumber, exam.percentile as percentile")
 fun findByExamTypeAndDate(classId: Long, subjectId: Long, year: Int, orgId: Long, examType: ExamType): List<ResultUpdateQueryResult>

}