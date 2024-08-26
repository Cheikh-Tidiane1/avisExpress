package com.tid.avisExpress.repository;

import com.tid.avisExpress.model.Jwt;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;
import java.util.stream.Stream;

public interface JwtRepository extends CrudRepository<Jwt, Integer> {

    Optional<Jwt> findByValeurAndDesactiveAndExpired(String valeur, Boolean desactive, Boolean expired);
    @Query("FROM Jwt j WHERE j.desactive = :desactive AND j.expired = :expired AND j.utilisateur.email = :email")
    Optional<Jwt> findByUtilisateurValidToken(String email, Boolean desactive, Boolean expired);
    @Query("FROM Jwt j WHERE j.utilisateur.email = :email")
    Stream<Jwt> findByUtilisateur(String email);
    @Query("FROM Jwt j WHERE j.refreshToken.value = :value")
    Optional<Jwt> findByRefreshToken (String value);
    void deleteAllByExpiredAndDesactive(Boolean expired, Boolean desactive);

}
