package com.lynas

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.filter.CommonsRequestLoggingFilter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import springfox.documentation.builders.ParameterBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.schema.ModelRef
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Parameter
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.*


/**
 * Created by lynas on 3/18/2017
 */

@EnableSwagger2
@SpringBootApplication
class Application : WebMvcConfigurerAdapter() {

    @Value("\${jwt.header}")
    private val tokenHeader: String = "Authorization"

    @Bean
    fun logFilter(): CommonsRequestLoggingFilter {
        val filter = CommonsRequestLoggingFilter()
        filter.setIncludeQueryString(true)
        filter.setIncludePayload(true)
        filter.setMaxPayloadLength(10000)
        filter.isIncludeHeaders = true
        filter.setAfterMessagePrefix("REQUEST DATA : ")
        return filter
    }

    private fun generateApiInfo(): ApiInfo {
        return ApiInfo("EOMS",
                "ERP system for Educational organization management system", "Version 1.0",
                "free", "szlynas@gmail.com", "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0")
    }

    @Bean
    fun docket(): Docket {
        val parameter = ParameterBuilder()
                .name(tokenHeader)
                .description("Authentication and authorization token header")
                .modelRef(ModelRef("string"))
                .parameterType("header")
                .required(false).build()
        val operationParameters = ArrayList<Parameter>()
        operationParameters.add(parameter)
        return Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(operationParameters)
                .select()
                .apis(RequestHandlerSelectors.basePackage(javaClass.`package`.name))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(generateApiInfo())
    }

}


fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}











