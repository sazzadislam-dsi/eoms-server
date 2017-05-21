package com.lynas.repo

import com.lynas.model.FeeInfo
import org.springframework.data.neo4j.annotation.Depth
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository

/**
 * Created by sazzad on 8/17/16
 */

interface FeeInfoRepository : GraphRepository<FeeInfo> {
    @Query("match (st:Student)-[r1:StudentFee]->(fi:FeeInfo)-[r2:feeInfoOfCourse]->(cls:Class)-" +
            "[r3:classBelongsToAnOrganization]->(org:Organization) where ID(st) = {0} " +
            "return st,fi,cls,org,r1,r2,r3")
    @Depth(3)
    fun findFeeInfo(studentId: Long): List<FeeInfo>?
}