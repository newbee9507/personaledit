package com.seb_main_034.SERVER.auth.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreTokenRepository extends JpaRepository<RefreToken, Long> {

    Optional<RefreToken> findByToken(String token);
}
