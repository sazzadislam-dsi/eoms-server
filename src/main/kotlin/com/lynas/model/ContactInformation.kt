package com.lynas.model

import com.lynas.model.util.ContactType
import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity

/**
 * Created by sazzad on 8/11/16
 */

@NodeEntity
class ContactInformation(
        @GraphId
        var id: Long? = null,
        var name: String,
        var address: String,
        var phone_1: String,
        var phone_2: String,
        var phone_3: String,
        var contactType: ContactType) {
    constructor() : this(null, "", "", "", "", "", ContactType.PRIMARY)
}