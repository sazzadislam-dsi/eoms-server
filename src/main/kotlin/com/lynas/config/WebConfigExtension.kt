package com.lynas.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
open class WebConfigExtension constructor(
        val interceptorConfig: InterceptorConfig) : WebMvcConfigurerAdapter() {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(interceptorConfig)
    }
}

















