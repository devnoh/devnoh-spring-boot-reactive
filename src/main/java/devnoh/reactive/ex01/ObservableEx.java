package devnoh.reactive.ex01;

import java.util.Observable;
import java.util.Observer;

/**
 * Observable (Push)
 */
public class ObservableEx {

    static class IntObservable extends Observable implements Runnable {
        @Override
        public void run() {
            for (int i = 1; i <= 10; i++) {
                setChanged();
                notifyObservers(i); // push
            }
        }
    }

    public static void main(String[] args) {

        Observer observer = new Observer() {
            @Override public void update(Observable o, Object arg) {
                System.out.println(arg);
            }
        };

        IntObservable observable = new IntObservable();
        observable.addObserver(observer);
        observable.run();
    }
}
