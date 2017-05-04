package com.lynas.model.response

/**
 * Created by seal on 5/4/2017.
 */
class ExamClassResponse {
    var roll: Int? = 0
    var name: String? = ""
    var resultOfSubjects: Map<String?, Double> = mutableMapOf()
}