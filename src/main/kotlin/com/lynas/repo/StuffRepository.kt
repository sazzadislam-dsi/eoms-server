package com.lynas.repo

import com.lynas.model.Stuff
import org.springframework.data.neo4j.repository.GraphRepository

/**
 * Created by sazzad on 8/17/16
 */

interface StuffRepository : GraphRepository<Stuff> {

    //fun findTeacherListByOrganization(orgId : Long) : List<Teacher>

}