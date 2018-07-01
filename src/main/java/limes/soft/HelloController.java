package limes.soft;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class HelloController {

    @Autowired
    BankaRepository bankaRepository;

    @GetMapping(value = "/getBanks")
    public List<Banka> selectTest(){
        return bankaRepository.findAll();
    }
    @GetMapping(value = "/banks")
    public List<Banka> selectTestMock(){
        List<Banka> banks = new ArrayList<>();
        Random rand = new Random();
        for(int i =0;i < 1000;i++){
            banks.add(new Banka(Integer.toString(i),"Novi Sad Banka"+Integer.toString(i), "340-255123-12312"+Integer.toString(i),i%(rand.nextInt(50) + 1)));
        }



        return banks;
    }
    @GetMapping(value = "/hello")
    public String hello(){
        return "hello";
    }

}
