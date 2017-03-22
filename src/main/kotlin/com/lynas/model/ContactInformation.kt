package com.lynas.model

import com.lynas.model.util.ContactType
import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity

/**
 * Created by sazzad on 8/11/16
 */

@NodeEntity
class ContactInformation {
    @GraphId
    var id: Long? = null
    var name: String? = null
    var address: String? = null
    var phone_1: String? = null
    var phone_2: String? = null
    var phone_3: String? = null
    var contactType: ContactType = ContactType.PRIMARY
    override fun toString(): String {
        return "ContactInformation(id=$id, name=$name, address=$address, phone_1=$phone_1, phone_2=$phone_2, phone_3=$phone_3, contactType=$contactType)"
    }


}