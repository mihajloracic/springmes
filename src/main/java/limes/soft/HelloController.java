package limes.soft;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    @Autowired
    BankaRepository bankaRepository;

    @GetMapping(value = "/getBanks")
    public List<Banka> selectTest(){
        return bankaRepository.findAll();
    }
    @GetMapping(value = "/hello")
    public String hello(){
        return "hello";
    }

}
