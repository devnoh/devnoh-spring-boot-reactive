package devnoh.reactive.ex04;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Async - Future
 */
@Slf4j
public class FutureEx1 {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newCachedThreadPool();

        es.execute(() -> { // Runnable
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("Async");
        });

        log.debug("Exit");
    }
}
