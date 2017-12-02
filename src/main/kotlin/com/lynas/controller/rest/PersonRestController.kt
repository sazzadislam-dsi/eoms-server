package com.lynas.controller.rest

import com.lynas.exception.EntityNotFoundException
import com.lynas.model.Person
import com.lynas.model.util.PersonContact
import com.lynas.service.PersonService
import com.lynas.util.getOrganizationFromSession
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/15/16
 */

@RestController
@RequestMapping("persons")
class PersonRestController(val personService: PersonService) {

    @PostMapping
    fun post(@RequestBody person: Person, request: HttpServletRequest): Person {
        person.organization = getOrganizationFromSession(request)
        personService.create(person)
        return person
    }

    @PatchMapping
    fun patch(@RequestBody person: Person): Person? {
        if (null != person.id) {
            val personFromDB = personService.findPersonById(person.id as Long) ?: throw EntityNotFoundException(
                    "Person info not found for person id [$person.id]"
            )
            personFromDB?.apply {
                firstName = person.firstName
                lastName = person.lastName
                dateOfBirth = person.dateOfBirth
                sex = person.sex
            }
            personService.create(personFromDB)
            return personFromDB
        }
        return person
    }


    @PostMapping("/contactInformation")
    fun updatePersonContactInfo(@RequestBody personContact: PersonContact) {
        val person = personService.findPersonById(personContact.personId)
        val contactInformationList = person?.contactInformationList
        contactInformationList?.add(personContact.contactInformation)
    }
}











