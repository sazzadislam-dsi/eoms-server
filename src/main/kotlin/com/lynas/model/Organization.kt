package com.lynas.model

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship

/**
 * Created by LynAs on 7/23/2016
 */
@NodeEntity
class Organization {
    @GraphId
    var id: Long? = null
    var name: String? = null
    var establishmentYear: Int? = null
    @Relationship(type = "organizationHasOrganizationInfo", direction = Relationship.OUTGOING)
    var organizationInfo: OrganizationInfo? = null

    override fun toString(): String {
        return "Organization(id=$id, name=$name, establishmentYear=$establishmentYear, organizationInfo=$organizationInfo)"
    }


}
