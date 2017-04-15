package devnoh.demoapp.ex04;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@RestController
@EnableAsync
@Slf4j
public class AsyncController {

    @RequestMapping("/async")
    public String async() throws InterruptedException {
        log.info("async");
        Thread.sleep(2000);
        return "Hello";
    }

    @RequestMapping("/callable")
    public Callable<String> callable() throws InterruptedException {
        log.info("callable");
        return () -> {
            log.info("async");
            Thread.sleep(2000);
            return "Hello";
        };
    }
}
