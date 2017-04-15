package devnoh.demoapp.ex04;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

@RestController
@EnableAsync
@Slf4j
public class DeferredController {

    Queue<DeferredResult<String>> results = new ConcurrentLinkedDeque<>();

    @RequestMapping("/deferred")
    public DeferredResult<String> deferred() throws InterruptedException {
        log.info("deferred");
        DeferredResult<String> dr = new DeferredResult<>();
        results.add(dr);
        return dr;
    }

    @RequestMapping("/deferred/count")
    public String deferredCount() throws InterruptedException {
        log.info("deferred event");
        return String.valueOf(results.size());
    }

    @RequestMapping("/deferred/event")
    public String deferredEvent(String msg) throws InterruptedException {
        log.info("deferred event");
        for (DeferredResult<String> dr : results) {
            dr.setResult("Hello " + msg);
            results.remove(dr);
        }
        return "OK";
    }
}
