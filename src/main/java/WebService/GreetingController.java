package WebService;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

    private  final static String template="HELLO, %s !";
    private  final AtomicLong atomicLong=new AtomicLong();

    @RequestMapping(method = RequestMethod.GET ,value = "/greeting")
    //REQUEST PARAM IS DIFFERENT FROM PATH PARAM
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "world") String name){
        return new Greeting(atomicLong.incrementAndGet(),String.format(template,name));

    }
}
