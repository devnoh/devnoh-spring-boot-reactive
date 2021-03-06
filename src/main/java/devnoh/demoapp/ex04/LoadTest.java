package devnoh.demoapp.ex04;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LoadTest {

    private static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(100);

        //String url = "http://localhost:8080/async"; // 10.xx secs
        //String url = "http://localhost:8080/callable"; // 2.xx secs
        String url = "http://localhost:8080/deferred"; // until result set

        RestTemplate rest = new RestTemplate();

        StopWatch sw = new StopWatch();
        sw.start();

        for (int i = 0; i < 100; i++) {
            es.execute(() -> {
                int idx = counter.addAndGet(1);
                log.info("Thread {}", idx);

                StopWatch sw2 = new StopWatch();
                sw2.start();

                rest.getForObject(url, String.class);

                sw2.stop();
                log.info("Elapsed: {} {}", idx, sw2.getTotalTimeSeconds());
            });
        }

        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);

        sw.stop();
        log.info("Total: {}", sw.getTotalTimeSeconds());
    }
}
