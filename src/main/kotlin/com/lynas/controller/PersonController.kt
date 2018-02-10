package com.lynas.controller

import com.lynas.dto.PersonContactDTO
import com.lynas.exception.EntityNotFoundException
import com.lynas.model.ContactInformation
import com.lynas.model.Person
import com.lynas.service.PersonService
import com.lynas.util.AuthUtil
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/15/16
 */

@RestController
@RequestMapping("persons")
class PersonController(val personService: PersonService, val authUtil: AuthUtil) {

    @PostMapping
    fun post(@RequestBody person: Person, request: HttpServletRequest): Person {
        person.organization = authUtil.getOrganizationFromToken(request)
        personService.create(person)
        return person
    }

    @PatchMapping
    fun patch(@RequestBody person: Person): Person? {
        if (null != person.id) {
            val personFromDB = personService.findPersonById(person.id as Long) ?: throw EntityNotFoundException(
                    "Person info not found for person id [$person.id]"
            )
            personFromDB.apply {
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
    fun updatePersonContactInfo(@RequestBody personContactDTO: PersonContactDTO): MutableList<ContactInformation>? {
        val person = personService.findPersonById(personContactDTO.personId)
        val contactInformationList = person?.contactInformationList
        contactInformationList?.add(personContactDTO.contactInformation)
        //todo no db operation ?
        return contactInformationList
    }
}











