package com.lynas.model

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity

/**
 * Created by LynAs on 7/23/2016
 */
@NodeEntity
data class OrganizationInfo(
        @GraphId
        var id: Long? = null,
        var founderName: String,
        var founderDescription: String)