package com.lynas.repo

import com.lynas.model.FeeInfo
import com.lynas.model.StudentFee
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository

/**
 * Created by sazzad on 8/17/16
 */

interface FeeInfoRepository : GraphRepository<FeeInfo> {
    @Query("match (st:Student)-[r1:StudentFee]->(fi:FeeInfo)-[r2:feeInfoOfCourse]->(cls:Class)-" +
            "[r3:classBelongsToAnOrganization]->(org:Organization) where ID(st) = {0} " +
            "return st,fi,cls,org,r1,r2,r3")
    fun findFeeInfoByStudent(id: Long): List<FeeInfo>?

    @Query("match (st:Student)-[r1:StudentFee]->(fi:FeeInfo)-[r2:feeInfoOfCourse]->(cls:Class)-" +
            "[r3:classBelongsToAnOrganization]->(org:Organization) where ID(cls) = {0} " +
            "return st,fi,cls,org,r1,r2,r3")
    fun findFeeInfoByClass(id: Long): List<FeeInfo>?

    @Query("match (st:Student)-[r1:StudentFee]->(fi:FeeInfo)-[r2:feeInfoOfCourse]->(cls:Class)-" +
            "[r3:classBelongsToAnOrganization]->(org:Organization) where ID(cls) = {0} " +
            "return st,fi,cls,org,r1,r2,r3")
    fun findStudentFeeInfoByClass(id: Long): List<StudentFee>?

    @Query("match (st:Student)-[r1:StudentFee]->(fi:FeeInfo)-[r2:feeInfoOfCourse]->(cls:Class)-" +
            "[r3:classBelongsToAnOrganization]->(org:Organization) where ID(st) = {0} " +
            "return st,fi,cls,org,r1,r2,r3")
    fun findStudentFeeInfoByStudent(id: Long): List<StudentFee>?
}