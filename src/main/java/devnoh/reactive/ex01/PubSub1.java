package devnoh.reactive.ex01;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Reactive Streams
 *
 * Publisher <- Observable
 * Subscriber <- Observer
 */
public class PubSub1 {

    public static void main(String[] args) {
        Iterable<Integer> iterable = Arrays.asList(1, 2, 3, 4, 5);

        Publisher publisher = new Publisher() {
            @Override
            public void subscribe(Subscriber subscriber) {
                Iterator<Integer> iterator = iterable.iterator();

                subscriber.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        try {
                            while (n-- > 0) {
                                if (iterator.hasNext()) {
                                    subscriber.onNext(iterator.next());
                                } else {
                                    subscriber.onComplete();
                                    break;
                                }
                            }
                        } catch (RuntimeException e) {
                            subscriber.onError(e);
                        }
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };

        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                System.out.println("onSubscribe ");
                this.subscription = subscription;
                this.subscription.request(1);
            }

            @Override
            public void onNext(Integer item) {
                System.out.println("onNext " + item);
                this.subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError ");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete ");
            }
        };

        publisher.subscribe(subscriber);

    }
}
