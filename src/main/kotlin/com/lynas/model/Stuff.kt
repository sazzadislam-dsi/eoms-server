package com.lynas.model

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship

/**
 * Created by sazzad on 8/8/16
 */
@NodeEntity
class Stuff {
    @GraphId
    var id: Long? = null
    var stuffID: String? = null
    var title: String? = null

    @Relationship(type = "stuffIsAPerson")
    var person: Person? = null
}