package com.lynas.model.request

/**
 * Created by sazzad on 5/18/17.
 */

data class FeeInfoJson(
        val classId: Long = 99999,
        val type: String = "",
        val amount: Double = 0.0,
        val year: Int = 2017,
        val lastDate: String? = null)
