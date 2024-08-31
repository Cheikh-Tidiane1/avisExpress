package com.tid.avisExpress.controller;
import com.tid.avisExpress.model.Avis;
import com.tid.avisExpress.model.Utilisateur;
import com.tid.avisExpress.services.AvisService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class AvisController {

    private AvisService avisService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/createAvis")
    public void createAvis(@RequestBody  Avis avis) {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        avis.setUtilisateur(utilisateur);
        this.avisService.createAvis(avis);
    }


    @GetMapping(path = "/allAvis")
    public Iterable<Avis> listAvis() {
        return this.avisService.listAvis();
    }
}
