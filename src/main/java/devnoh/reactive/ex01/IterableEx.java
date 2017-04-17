package devnoh.reactive.ex01;

import java.util.Iterator;

/**
 * Iterable (Pull)
 */
public class IterableEx {

    public static void main(String[] args) {

        Iterable<Integer> iterable = new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return new Iterator<Integer>() {
                    static final int MAX = 10;
                    int i = 0;

                    @Override
                    public boolean hasNext() {
                        return i < MAX;
                    }

                    @Override
                    public Integer next() {
                        return ++i;
                    }
                };
            }
        };

        for (Iterator<Integer> iterator = iterable.iterator(); iterator.hasNext(); ) {
            System.out.println(iterator.next()); // pull
        }

        /*
        Iterable<Integer> iterable2 = () -> {
            return new Iterator<Integer>() {
                static final int MAX = 10;
                int i = 0;

                @Override
                public boolean hasNext() {
                    return i < MAX;
                }

                @Override
                public Integer next() {
                    return ++i;
                }
            };
        };

        for (Integer i : iterable2) {
            System.out.println(i);
        }
        */
    }
}
