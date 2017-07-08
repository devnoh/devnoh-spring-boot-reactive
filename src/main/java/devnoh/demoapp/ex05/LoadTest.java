package devnoh.demoapp.ex05;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LoadTest {

    private static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        ExecutorService es = Executors.newFixedThreadPool(100);

        //String url = "http://localhost:8080/rest4?idx={idx}";
        String url = "http://localhost:8080/rest6?idx={idx}";
        RestTemplate rest = new RestTemplate();

        CyclicBarrier barrier = new CyclicBarrier(101);

        StopWatch sw = new StopWatch();
        sw.start();

        for (int i = 0; i < 100; i++) {
            es.submit(() -> {
                int idx = counter.addAndGet(1);

                barrier.await();

                log.info("Thread {}", idx);

                StopWatch sw2 = new StopWatch();
                sw2.start();

                String res = rest.getForObject(url, String.class, idx);

                sw2.stop();
                log.info("Elapsed: {} {} / {}", idx, sw2.getTotalTimeSeconds(), res);

                return null;
            });
        }

        barrier.await();

        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);

        sw.stop();
        log.info("Total: {}", sw.getTotalTimeSeconds());
    }
}
