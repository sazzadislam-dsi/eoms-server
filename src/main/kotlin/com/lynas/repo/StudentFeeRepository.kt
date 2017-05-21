package com.lynas.repo

import com.lynas.model.StudentFee
import org.springframework.data.neo4j.repository.GraphRepository

/**
 * Created by sazzad on 7/18/16
 */

interface StudentFeeRepository : GraphRepository<StudentFee>
