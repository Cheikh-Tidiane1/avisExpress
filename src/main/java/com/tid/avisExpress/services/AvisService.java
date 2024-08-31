package com.tid.avisExpress.services;
import com.tid.avisExpress.model.Avis;
import com.tid.avisExpress.repository.AvisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AvisService {
    private AvisRepository avisRepository;

    @Autowired
    public AvisService(AvisRepository avisRepository) {
        this.avisRepository = avisRepository;
    }

    public void createAvis(Avis avis) {
        avisRepository.save(avis);
    }


    public Iterable<Avis> listAvis() {
        return this.avisRepository.findAll();
    }
}
