package devnoh.reactive.ex02;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
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
public class PubSub7 {

    public static void main(String[] args) {
        Publisher<Integer> pub = iterPub(Stream.iterate(1, a -> a + 1).limit(10).collect(Collectors.toList()));
        Publisher<String> mapPub = mapPub(pub, s -> "[" + (s * 10) + "]");
        mapPub.subscribe(logSub());
        Publisher<String> reducePub = reducePub(pub, "", (a, b) -> a + "-" + b);
        reducePub.subscribe(logSub());
        Publisher<StringBuffer> reducePub2 = reducePub(pub, new StringBuffer(), (a, b) -> a.append(b).append(","));
        reducePub2.subscribe(logSub());
    }

    // 1, 2, 3, 4, 5
    // 0 -> (0, 1) -> 0 + 1 = 1
    // 1 -> (1, 2) -> 1 + 2 = 3
    // 3 -> (3, 3) -> 3 + 3 = 6
    // 6 -> ...

//    private static Publisher<String> reducePub(Publisher<Integer> pub, String init,
//            BiFunction<String, Integer, String> bf) {
//        return new Publisher<String>() {
//            @Override
//            public void subscribe(Subscriber<? super String> sub) {
//                pub.subscribe(new DelegateSub3<Integer, String>(sub) {
//                    String result = init;
//
//                    @Override
//                    public void onNext(Integer i) {
//                        result = bf.apply(result, i);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        sub.onNext(result);
//                        sub.onComplete();
//                    }
//                });
//            }
//        };
//    }

    private static <T, R> Publisher<R> reducePub(Publisher<T> pub, R init, BiFunction<R, T, R> bf) {
        return new Publisher<R>() {
            @Override
            public void subscribe(Subscriber<? super R> sub) {
                pub.subscribe(new DelegateSub3<T, R>(sub) {
                    R result = init;

                    @Override
                    public void onNext(T i) {
                        result = bf.apply(result, i);
                    }

                    @Override
                    public void onComplete() {
                        sub.onNext(result);
                        sub.onComplete();
                    }
                });
            }
        };
    }

    private static <T, R> Publisher<R> mapPub(Publisher<T> pub, Function<T, R> f) {
        return new Publisher<R>() {
            @Override
            public void subscribe(Subscriber<? super R> sub) {
                pub.subscribe(new DelegateSub3<T, R>(sub) {
                    @Override
                    public void onNext(T i) {
                        sub.onNext(f.apply(i));
                    }
                });
            }
        };
    }

    private static <T> Subscriber<T> logSub() {
        return new Subscriber<T>() {
            @Override
            public void onSubscribe(Subscription s) {
                log.debug("onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(T i) {
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
