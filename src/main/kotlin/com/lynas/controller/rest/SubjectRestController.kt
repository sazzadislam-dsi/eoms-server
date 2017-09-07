package com.lynas.controller.rest

import com.lynas.model.Subject
import com.lynas.model.request.SubjectPostJson
import com.lynas.service.ClassService
import com.lynas.service.SubjectService
import com.lynas.util.getLogger
import com.lynas.util.getOrganizationFromSession
import com.lynas.util.responseError
import com.lynas.util.responseOK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

/**
 * Created by seal on 2/7/2017.
 */

@RestController
@RequestMapping("/subjects")
class SubjectRestController constructor(val subjectService: SubjectService,
                                        val classService: ClassService) {

    val logger = getLogger(this.javaClass)

    @PostMapping
    fun post(@RequestBody subjectJson: SubjectPostJson, request: HttpServletRequest): ResponseEntity<*> {
        logger.info("Hit create method with {}", subjectJson)
        val subject: Subject = Subject().apply {
            subjectName = subjectJson.subjectName
            subjectDescription = subjectJson.subjectDescription
            subjectBookAuthor = subjectJson.subjectBookAuthor

        }

        // Problem in save object
        classService
                .findById(subjectJson.classId as Long, getOrganizationFromSession(request).id!!)
                ?.let {
                    subject.cls = it
                    subjectService.create(subject)
                    logger.info("Post subject successful for class id [{}]", it.id)
                    return responseOK(subjectJson)
                }

        return responseError(subjectJson)
    }

}