package com.lynas.repo

import com.lynas.model.ContactInformation
import org.springframework.data.neo4j.repository.GraphRepository

/**
 * Created by sazzad on 7/18/16
 */

interface ContactInformationRepository : GraphRepository<ContactInformation>