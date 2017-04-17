package devnoh.reactive.ex02;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class DelegateSub3<T, R> implements Subscriber<T> {

    private Subscriber sub;

    public DelegateSub3(Subscriber<? super R> sub) {
        this.sub = sub;
    }

    @Override
    public void onSubscribe(Subscription s) {
        sub.onSubscribe(s);
    }

    @Override
    public void onNext(T i) {
        sub.onNext(i);
    }

    @Override
    public void onError(Throwable t) {
        sub.onError(t);
    }

    @Override
    public void onComplete() {
        sub.onComplete();
    }
}
