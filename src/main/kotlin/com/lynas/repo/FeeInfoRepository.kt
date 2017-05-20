package com.lynas.repo

import com.lynas.model.FeeInfo
import org.springframework.data.neo4j.repository.GraphRepository

/**
 * Created by sazzad on 8/17/16
 */

interface FeeInfoRepository : GraphRepository<FeeInfo>