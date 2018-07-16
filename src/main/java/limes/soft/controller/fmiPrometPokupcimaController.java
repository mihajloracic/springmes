package limes.soft.controller;

import limes.soft.repository.fmiPrometPokupcimaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class fmiPrometPokupcimaController {

    @Autowired
    fmiPrometPokupcimaRepository repository;

    @GetMapping(value = "/fmiPrometPokupcimaRequest")
    public String getAllmasterdetail(){
        return repository.findlAll().toString();
    }

}
