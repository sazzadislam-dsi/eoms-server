package com.lynas.model

import com.lynas.model.util.Section
import com.lynas.model.util.Shift
import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship


/**
 * Created by LynAs on 7/23/2016
 */
@NodeEntity(label = "Class")
class Course {
    @GraphId
    var id: Long? = null
    var name: String? = null
    var shift: Shift = Shift.MORNING
    var section: Section = Section.SECTION_1

    @Relationship(type = "classBelongsToAnOrganization", direction = Relationship.OUTGOING)
    var organization: Organization? = null
}