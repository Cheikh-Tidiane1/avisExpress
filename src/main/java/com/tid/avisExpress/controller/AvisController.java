package com.tid.avisExpress.controller;
import com.tid.avisExpress.model.Avis;
import com.tid.avisExpress.services.AvisService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("AvisExpress")
public class AvisController {

    private AvisService avisService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/createAvis")
    public void createAvis(@RequestBody  Avis avis) {
        this.avisService.createAvis(avis);
    }
}
