package limes.soft;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    DBRepository DBRepository;

    @GetMapping(value = "/fa")
    public String asd(@RequestParam String startDate, @RequestParam String endDate){
        return DBRepository.findlAllPregledTroskova(startDate,endDate).toString();
    }
    @GetMapping(value="/kupcidobavljaci")
    public String pregledTroskova(){
        return DBRepository.findlAllKupcidobavljaci().toString();
    }
}
