package devnoh.demoapp.ex04;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.concurrent.Executors;

@RestController
@EnableAsync
@Slf4j
public class EmitterController {

    @RequestMapping("/emitter")
    public ResponseBodyEmitter emitter() throws InterruptedException {
        log.info("emitter");
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                for (int i = 1; i <= 50; i++) {
                    emitter.send("<p>Stream " + i + "</p>");
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return emitter;
    }
}
