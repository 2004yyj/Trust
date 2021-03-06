package kr.hs.dgsw.trust.server.repository

import kr.hs.dgsw.trust.server.data.vo.AccountVO
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<AccountVO, String?>