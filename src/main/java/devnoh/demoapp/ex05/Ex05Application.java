package devnoh.demoapp.ex05;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

@SpringBootApplication
@EnableAsync
@Slf4j
public class Ex05Application {

    @RestController
    public static class MyController {

        @Autowired
        MyService myService;

        RestTemplate rt = new RestTemplate();
        AsyncRestTemplate rt2 = new AsyncRestTemplate();
        AsyncRestTemplate rt3 = new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1)));

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

        @GetMapping("/rest3")
        public ListenableFuture<ResponseEntity<String>> rest3(int idx) {
            ListenableFuture<ResponseEntity<String>> res =
                    rt3.getForEntity("http://localhost:8081/service?req={req}", String.class, "hello" + idx);
            return res;
        }

        @GetMapping("/rest4")
        public DeferredResult<String> rest4(int idx) {
            DeferredResult<String> dr = new DeferredResult<>();

            ListenableFuture<ResponseEntity<String>> res =
                    rt3.getForEntity("http://localhost:8081/service?req={req}", String.class, "hello" + idx);
            res.addCallback(s -> {
                dr.setResult(s.getBody() + "/work");
            }, e -> {
                dr.setErrorResult(e.getMessage());
            });
            return dr;
        }

        @GetMapping("/rest5")
        public DeferredResult<String> rest5(int idx) {
            DeferredResult<String> dr = new DeferredResult<>();

            ListenableFuture<ResponseEntity<String>> res =
                    rt3.getForEntity("http://localhost:8081/service?req={req}", String.class, "hello" + idx);
            res.addCallback(s -> {
                ListenableFuture<ResponseEntity<String>> res2 =
                        rt3.getForEntity("http://localhost:8081/service2?req={req}", String.class, s.getBody());
                res2.addCallback(s2 -> {
                    dr.setResult(s2.getBody());
                }, e2 -> {
                    dr.setErrorResult(e2.getMessage());
                });
            }, e -> {
                dr.setErrorResult(e.getMessage());
            });
            return dr;
        }

        @GetMapping("/rest6")
        public DeferredResult<String> rest6(int idx) {
            DeferredResult<String> dr = new DeferredResult<>();

            ListenableFuture<ResponseEntity<String>> res =
                    rt3.getForEntity("http://localhost:8081/service?req={req}", String.class, "hello" + idx);
            res.addCallback(s -> {
                ListenableFuture<ResponseEntity<String>> res2 =
                        rt3.getForEntity("http://localhost:8081/service2?req={req}", String.class, s.getBody());
                res2.addCallback(s2 -> {
                    ListenableFuture<String> res3 = myService.work(s2.getBody());
                    res3.addCallback(s3 -> {
                        dr.setResult(s3);
                    }, e3 -> {
                        dr.setErrorResult(e3.getMessage());
                    });
                }, e2 -> {
                    dr.setErrorResult(e2.getMessage());
                });
            }, e -> {
                dr.setErrorResult(e.getMessage());
            });
            return dr;
        }

    }

    @Service
    public static class MyService {
        @Async
        public ListenableFuture<String> work(String req) {
            return new AsyncResult<>(req + "/asyncwork");
        }
    }

    @Bean
    public ThreadPoolTaskExecutor threadPool() {
        ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
        te.setCorePoolSize(1);
        te.setMaxPoolSize(10);
        te.initialize();
        return te;
    }

    public static void main(String[] args) {
        System.setProperty("server.tomcat.max-threads", "1");
        SpringApplication.run(Ex05Application.class, args);
    }

}
