package com.lynas.config

import com.github.mxab.thymeleaf.extras.dataattribute.dialect.DataAttributeDialect
import org.apache.log4j.Logger
import org.springframework.beans.factory.InjectionPoint
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
class WebConfigExtension constructor(
        val interceptorConfig: InterceptorConfig) : WebMvcConfigurerAdapter() {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(interceptorConfig)
    }

    @Bean
    @Scope("prototype")
    fun logger(injectionPoint: InjectionPoint): Logger {
        return Logger.getLogger(injectionPoint.member.declaringClass)
    }
}

















