package com.lynas.service

import com.lynas.model.Stuff
import com.lynas.repo.PersonRepository
import com.lynas.repo.StuffRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by sazzad on 7/19/16
 */

@Service
open class StuffService(
        val stuffRepository: StuffRepository,
        val personRepository: PersonRepository) {

    @Transactional
    open fun save(stuff: Stuff) {
        stuff.person = personRepository.save(stuff.person)
        stuffRepository.save(stuff)
    }
}