package limes.soft.controller;

import limes.soft.repository.IzvestajTroskovaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IzvestajTroskovaController {

    @Autowired
    IzvestajTroskovaRepository repository;

    @GetMapping(value = "/IzvestajTroskovaRequest")
    public String getAllmasterdetail(){
        return repository.findlAll().toString();
    }

}
