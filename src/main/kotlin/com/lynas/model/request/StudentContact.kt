package com.lynas.model.request

import com.lynas.model.util.ContactType

/**
 * Created by lynas on 11/30/2016
 */

data class StudentContact(
        val studentId: Long = 1,
        val name: String = "",
        val address: String = "",
        val phone_1: String = "",
        val phone_2: String = "",
        val phone_3: String = "",
        val contactType: ContactType = ContactType.PRIMARY

)