package com.lynas.model.request

import com.lynas.model.util.Religion
import com.lynas.model.util.Sex

/**
 * Created by lynas on 10/16/2016
 */
data class StudentJson (
        var studentId: Long = 1,
        var firstName: String = "",
        var lastName: String = "",
        var dateOfBirth: String = "",
        var sex: Sex = Sex.MALE,
        var religion: Religion = Religion.MUSLIM
)