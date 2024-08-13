package com.tid.avisExpress.repository;

import com.tid.avisExpress.model.Jwt;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface JwtRepository extends CrudRepository<Jwt, Integer> {
    Optional<Jwt> findByValeur(String value);
}
