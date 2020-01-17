import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("test")
public class TestController {
    @RequestMapping("demo")
    public String testMethod(){
        return "hello world";
    }
}
