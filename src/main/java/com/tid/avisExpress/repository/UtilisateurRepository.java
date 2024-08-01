package com.tid.avisExpress.repository;

import com.tid.avisExpress.model.Utilisateur;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends CrudRepository<Utilisateur, Integer> {
}
