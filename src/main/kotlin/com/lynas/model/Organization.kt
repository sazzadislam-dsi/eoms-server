package com.lynas.model

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship

/**
 * Created by LynAs on 7/23/2016
 */
@NodeEntity
class Organization(
        @GraphId
        var id: Long? = null,
        var name: String,
        var establishmentYear: Int,
        @Relationship(type = "organizationHasOrganizationInfo", direction = Relationship.OUTGOING)
        var organizationInfo: OrganizationInfo)