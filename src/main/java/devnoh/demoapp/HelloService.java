package devnoh.demoapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Future;

@Service
@EnableAsync
@Slf4j
public class HelloService {
    public String hello() throws InterruptedException {
        log.info("hello()");
        Thread.sleep(1000);
        return "Hello";
    }

    @Async
    public Future<String> helloAsync1() throws InterruptedException {
        log.info("helloAsync1()");
        Thread.sleep(1000);
        return new AsyncResult<>("Hello");
    }

    @Async
    public ListenableFuture<String> helloAsync2() throws InterruptedException {
        log.info("helloAsync2()");
        Thread.sleep(1000);
        return new AsyncResult<>("Hello");
    }
}
