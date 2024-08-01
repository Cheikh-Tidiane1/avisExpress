package com.tid.avisExpress.repository;

import com.tid.avisExpress.model.Avis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvisRepository extends CrudRepository<Avis, Integer> {
}
