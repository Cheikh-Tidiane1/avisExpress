package com.tid.avisExpress.repository;

import com.tid.avisExpress.model.Jwt;
import org.springframework.data.repository.CrudRepository;

public interface JwtRepository extends CrudRepository<Jwt, Integer> {
}
