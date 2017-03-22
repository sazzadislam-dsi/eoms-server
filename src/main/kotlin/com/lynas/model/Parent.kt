package com.lynas.model

import com.lynas.model.util.RelationshipType
import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship

/**
 * Created by sazzad on 8/11/16
 */
@NodeEntity
class Parent {
    @GraphId
    var id: Long? = null
    var name: String = ""
    var relationship: RelationshipType? = null
    @Relationship(type = "isAPerson")
    var person: Person? = null
    @Relationship(type = "parentHasListOfContactInformation")
    var contactInformationList: MutableList<ContactInformation>? = null
}