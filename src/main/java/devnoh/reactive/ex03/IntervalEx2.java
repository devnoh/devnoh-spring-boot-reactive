package devnoh.reactive.ex03;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class IntervalEx2 {

    public static void main(String[] args) {
        Publisher<Integer> pub = sub -> {
            sub.onSubscribe(new Subscription() {
                int no = 0;
                boolean cancelled = false;

                @Override public void request(long l) {
                    ScheduledExecutorService es = Executors.newSingleThreadScheduledExecutor();
                    es.scheduleAtFixedRate(() -> {
                        if (cancelled) {
                            es.shutdown();
                            return;
                        }
                        sub.onNext(no++);
                    }, 0, 300, TimeUnit.MILLISECONDS);
                }

                @Override public void cancel() {
                    cancelled = true;
                }
            });
        };

        Publisher<Integer> takePub = sub -> {
            pub.subscribe(new Subscriber<Integer>() {
                int count = 0;
                Subscription subsc;

                @Override
                public void onSubscribe(Subscription s) {
                    this.subsc = s;
                    sub.onSubscribe(s);
                }

                @Override public void onNext(Integer i) {
                    sub.onNext(i);
                    if (++count >= 5) {
                        this.subsc.cancel();
                    }
                }

                @Override public void onError(Throwable t) {
                    sub.onError(t);
                }

                @Override public void onComplete() {
                    sub.onComplete();
                }
            });
        };

        takePub.subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                log.debug("onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer i) {
                log.debug("onNext:{}", i);
            }

            @Override
            public void onError(Throwable t) {
                log.debug("onError:{}", t);
            }

            @Override
            public void onComplete() {
                log.debug("onComplete");
            }
        });

        System.out.println("exit");

    }
}
