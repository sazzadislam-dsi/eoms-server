package com.lynas.model.request

/**
 * Created by seal on 2/16/2017.
 */
data class ExamJson (
        val mark: Double = 0.0,
        val studentId: Long = 0,
        val isPresent: Boolean = true
)