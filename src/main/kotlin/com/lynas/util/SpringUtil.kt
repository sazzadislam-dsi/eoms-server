package com.lynas.util

import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

/**
* Created by lynas on 9/9/2016
*/

@Component
class SpringUtil(environment: Environment) {

    val appOrgName: String = environment.getProperty("app.org.name")

    fun getAppOrganizationName() = appOrgName

}

















