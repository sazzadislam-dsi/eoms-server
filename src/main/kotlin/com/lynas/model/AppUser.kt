package com.lynas.model

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship

/**
 * Created by LynAs on 7/23/2016
 */
/**
 * domain must be normal class coz it needs no arg constructor and field
 * needs to be var so that it can be re assign by neo4j ogm
 */


@NodeEntity
data class AppUser(
        @GraphId
        var id: Long? = null,
        var username: String,
        var password: String,
        var authorities: String,
        @Relationship(type = "appUserBelongsToAnOrganization", direction = Relationship.OUTGOING)
        var organization: Organization) {
    constructor() : this(0L, "", "", "", Organization())
}
