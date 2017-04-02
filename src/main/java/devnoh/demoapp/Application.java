package devnoh.demoapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Future;

@SpringBootApplication
@Slf4j
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
//        try (ConfigurableApplicationContext context = SpringApplication.run(Application.class, args)) {
//        }
    }

    @Autowired
    HelloService helloService;

    @Bean
    ApplicationRunner run() {
        return args -> {
            log.info("run()");
            String r = helloService.hello();
            log.info("r={}", r);

            Future<String> f = helloService.helloAsync1();
            log.info("f={}", f.isDone());
            log.info("f={}", f.get());
            log.info("f={}", f.isDone());

            ListenableFuture<String> l = helloService.helloAsync2();
            l.addCallback(s -> log.info("l={}", s), e -> log.error(e.getMessage()));

            log.info("exit");
        };
    }

}
