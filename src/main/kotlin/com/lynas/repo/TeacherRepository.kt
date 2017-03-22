package com.lynas.repo

import com.lynas.model.Teacher
import org.springframework.data.neo4j.repository.GraphRepository

/**
 * Created by sazzad on 8/17/16
 */

interface TeacherRepository : GraphRepository<Teacher> {

    //fun findTeacherListByOrganization(orgId : Long) : List<Teacher>

}