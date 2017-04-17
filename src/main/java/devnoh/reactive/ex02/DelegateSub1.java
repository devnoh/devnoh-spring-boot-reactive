package devnoh.reactive.ex02;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class DelegateSub1 implements Subscriber<Integer> {

    private Subscriber sub;

    public DelegateSub1(Subscriber sub) {
        this.sub = sub;
    }

    @Override
    public void onSubscribe(Subscription s) {
        sub.onSubscribe(s);
    }

    @Override
    public void onNext(Integer i) {
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
