package limes.soft.controller;

import limes.soft.repository.PrometPoKupcimaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrometPoKupcimaController {

    @Autowired
    PrometPoKupcimaRepository repository;

    @GetMapping(value = "/PrometPoKupcimaRequest")
    public String getAllmasterdetail(){
        return repository.findlAll().toString();
    }

}
