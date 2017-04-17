package devnoh.reactive.ex03;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FluxScEx2 {
    public static void main(String[] args) throws InterruptedException {
        // Daemon thread
        Flux.interval(Duration.ofMillis(500))
                .subscribe(s -> log.debug("onNext:{}", s));

        log.debug("exit");
        TimeUnit.SECONDS.sleep(5);

        /*
        // User thread
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("Hello");
        });

        log.debug("exit");
        */
    }
}
