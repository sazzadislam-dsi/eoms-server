package com.lynas.model

import com.lynas.model.util.Religion
import com.lynas.model.util.Sex
import com.lynas.util.convertToString
import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship
import org.neo4j.ogm.annotation.typeconversion.DateLong
import java.util.*

/**
 * Created by LynAs on 7/31/2016
 */


@NodeEntity
open class Person {
    @GraphId
    var id: Long? = null
    var firstName: String? = null
    var lastName: String? = null
    @DateLong
    var dateOfBirth: Date? = null
    var sex: Sex? = null
    var religion: Religion? = null

    @Relationship(type = "personBelongsToAnOrganization")
    var organization: Organization? = null

    @Relationship(type = "personHasListOfContactInformation", direction = Relationship.INCOMING)
    var contactInformationList: MutableList<ContactInformation>? = null

    override fun toString(): String {
        return "Person(firstName='$firstName', lastName='$lastName', dateOfBirth=$dateOfBirth, " +
                "sex=$sex, religion=$religion, organization=$organization, contactInformationList=$contactInformationList)"
    }

    fun dateInString():String = this.dateOfBirth!!.convertToString()

}
