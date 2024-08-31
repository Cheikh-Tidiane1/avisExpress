package com.tid.avisExpress.controller;

import com.tid.avisExpress.model.Utilisateur;
import com.tid.avisExpress.services.UtilisateurService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@Slf4j
public class UtilisateurController {

    private UtilisateurService utilisateurService;

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    @GetMapping(path = "/allUsers")
    public Iterable<Utilisateur> getAllUsers() {
        return this.utilisateurService.listUser();
    }
}
