package com.tid.avisExpress.repository;

import com.tid.avisExpress.model.PasswordReset;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
@Repository
public interface  PasswordResetRepository extends CrudRepository<PasswordReset, Long> {
    Optional<PasswordReset> findByCode(String code);
    void deleteAllByExpireBefore(Instant expireBefore);
}
