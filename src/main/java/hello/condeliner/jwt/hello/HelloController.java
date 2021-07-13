package hello.condeliner.jwt.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class HelloController {
    
    @GetMapping(path = "/hello")
    public String hello () {
        return "Hello";
    }
}
