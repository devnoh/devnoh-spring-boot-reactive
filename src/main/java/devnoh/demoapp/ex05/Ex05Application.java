package devnoh.demoapp.ex05;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@Slf4j
public class Ex05Application {

    @RestController
    public static class MyController {

        RestTemplate rt = new RestTemplate();
        AsyncRestTemplate rt2 = new AsyncRestTemplate();

        @GetMapping("/rest")
        public String rest(int idx) {
            String res = rt.getForObject("http://localhost:8081/service?req={req}", String.class, "hello" + idx);
            return res;
        }

        @GetMapping("/rest2")
        public ListenableFuture<ResponseEntity<String>> rest2(int idx) {
            ListenableFuture<ResponseEntity<String>> res =
                    rt2.getForEntity("http://localhost:8081/service?req={req}", String.class, "hello" + idx);
            return res;
        }

    }

    public static void main(String[] args) {
        System.setProperty("server.tomcat.max-threads", "1");
        SpringApplication.run(Ex05Application.class, args);
    }

}
