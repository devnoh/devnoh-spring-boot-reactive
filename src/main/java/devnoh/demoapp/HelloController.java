package devnoh.demoapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
public class HelloController {

    @Autowired
    HelloService helloService;

    @RequestMapping("/hello")
    public String hello(String name) {
        return "Hello " + name;
    }

    @RequestMapping("/hello2")
    public CompletableFuture<String> hello2(String name) {
        return CompletableFuture
                .supplyAsync(() -> "Hello " + name);
    }

    @RequestMapping("/hello3")
    public CompletableFuture<String> hello3(String name) {
        return CompletableFuture
                .supplyAsync(() -> "Hello " + name)
                .thenApplyAsync(s -> s.toUpperCase());
    }

    @RequestMapping("/hello4")
    public Mono<String> hello4(String name) {
        return Mono.just("Hello " + name);
    }

    @RequestMapping(value = "/hello5")
    public Mono<String> hello5(String name) {
        return Mono.just("Hello " + name)
                .map(s -> s.toUpperCase())
                .publishOn(Schedulers.newSingle("publishOn"))
                .log();
    }

    /*
    @RequestMapping("/hello4")
    public Publisher<String> hello4(String name) {
        return new Publisher<String>() {
            @Override
            public void subscribe(Subscriber<? super String> sub) {
                sub.onSubscribe(new Subscription() {
                    @Override
                    public void request(long l) {
                        sub.onNext("Hello " + name);
                        sub.onComplete();
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };
    }
    */

}
