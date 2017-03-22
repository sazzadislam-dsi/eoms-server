package com.lynas.model.request

/**
 * Created by seal on 12/29/2016.
 */
data class EnrolmentJson (
        var studentId: Long = 1,
        var classId: Long = 1,
        var year: Int = 1,
        var roleNumber: Int = 1
)