package kr.hs.dgsw.trust.server.repository

import kr.hs.dgsw.trust.server.data.entity.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, String?>