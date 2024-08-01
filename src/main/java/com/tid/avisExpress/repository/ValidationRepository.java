package com.tid.avisExpress.repository;
import com.tid.avisExpress.model.Validation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValidationRepository extends CrudRepository<Validation, Integer> {
}
