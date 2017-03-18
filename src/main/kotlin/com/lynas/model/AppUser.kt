package com.lynas.model

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity

/**
 * Created by LynAs on 7/23/2016
 */
/**
 * domain must be normal class coz it needs no arg constructor and field
 * needs to be var so that it can be re assign by neo4j ogm
 */
@NodeEntity
class AppUser {
    @GraphId
    var id: Long? = null
    var username: String? = null
    var password: String? = null
    var authorities: String? = null

    override fun toString(): String {
        return "AppUser(id=$id, username=$username, password=$password, authorities=$authorities)"
    }


}
