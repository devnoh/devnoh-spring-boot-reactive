package devnoh.reactive.ex02;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Reactive Streams - Operators
 *
 * Publisher -> Data -> Subscriber
 * Publisher -> [Data1] -> Operator1 -> [Data2] -> Operator2 -> [Data3] -> Subscriber
 * 1. map (d1 -> f -> d2)
 * pub -> [Data1] -> mapPub -> [Data2] -> logSub
 * <- subscribe(logsSub)
 * -> onSubscribe(s)
 * -> onNext
 * -> onNext
 * -> onComplete
 */
@Slf4j
public class PubSub4 {

    public static void main(String[] args) {
        Publisher<Integer> pub = iterPub(Stream.iterate(1, a -> a + 1).limit(10).collect(Collectors.toList()));
//        Publisher<Integer> mapPub = mapPub(pub, s -> s * 10);
//        mapPub.subscribe(logSub());
//        Publisher<Integer> sumPub = sumPub(pub);
//        sumPub.subscribe(logSub());
        // Publisher<Integer> reducePub = reducePub(pub, 0, (BiFunction<Integer, Integer, Integer>) (a, b) -> a + b);
        Publisher<Integer> reducePub = reducePub(pub, 0, (a, b) -> a + b);
        reducePub.subscribe(logSub());
    }

    // 1, 2, 3, 4, 5
    // 0 -> (0, 1) -> 0 + 1 = 1
    // 1 -> (1, 2) -> 1 + 2 = 3
    // 3 -> (3, 3) -> 3 + 3 = 6
    // 6 -> ...

    private static Publisher<Integer> reducePub(Publisher<Integer> pub, int init,
            BiFunction<Integer, Integer, Integer> bf) {
        return new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> sub) {
                pub.subscribe(new DelegateSub1(sub) {
                    int result = init;

                    @Override
                    public void onNext(Integer i) {
                        result = bf.apply(result, i);
                    }

                    @Override public void onComplete() {
                        sub.onNext(result);
                        sub.onComplete();
                    }
                });
            }
        };
    }

//    private static Publisher<Integer> sumPub(Publisher<Integer> pub) {
//        return new Publisher<Integer>() {
//            @Override
//            public void subscribe(Subscriber<? super Integer> sub) {
//                pub.subscribe(new DelegateSub1(sub) {
//                    int sum = 0;
//
//                    @Override public void onNext(Integer i) {
//                        sum += i;
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        sub.onNext(sum);
//                        sub.onComplete();
//                    }
//                });
//            }
//        };
//    }
//
//    private static Publisher<Integer> mapPub(Publisher<Integer> pub, Function<Integer, Integer> f) {
//        return new Publisher<Integer>() {
//            @Override
//            public void subscribe(Subscriber<? super Integer> sub) {
//                pub.subscribe(new DelegateSub1(sub) {
//                    @Override
//                    public void onNext(Integer i) {
//                        sub.onNext(f.apply(i));
//                    }
//                });
//            }
//        };
//    }

    private static Subscriber<Integer> logSub() {
        return new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                log.debug("onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer i) {
                log.debug("onNext: {}", i);
            }

            @Override
            public void onError(Throwable t) {
                log.debug("onError: {}", t);
            }

            @Override
            public void onComplete() {
                log.debug("onComplete");
            }
        };
    }

    private static Publisher<Integer> iterPub(List<Integer> iterator) {
        return new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> sub) {
                sub.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        try {
                            iterator.forEach(i -> sub.onNext(i));
                            sub.onComplete();
                        } catch (Throwable t) {
                            sub.onError(t);
                        }
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };
    }

}
