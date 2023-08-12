package com.draw.infra.persistence

import com.draw.domain.claim.Claim
import org.springframework.data.jpa.repository.JpaRepository

interface ClaimRepository : JpaRepository<Claim, Long>
