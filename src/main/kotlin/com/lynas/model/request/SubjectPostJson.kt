package com.lynas.model.request

/**
 * Created by seal on 2/7/2017.
 */
data class SubjectPostJson(

    var subjectName: String? = null,
    var subjectDescription: String? = null,
    var subjectBookAuthor: String? = null,
    var classId: Long? = null
)