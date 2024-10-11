package me.altzenck.utils;

public class RandomElement<E> {

    private final E element;
    private final float probability;

    private RandomElement(E element, float probability) {
        this.element = element;
        this.probability = probability;
    }

    public static <E> RandomElement<E> of(E element, float probability) {
        if(probability > 100 || probability < -2) throw new IllegalArgumentException("probability");
        return new RandomElement<>(element, probability);
    }

    public static <E> RandomElement<E> of(E element) {
        return of(element, -1);
    }

    public E getValue() {
        return element;
    }

    public float getProbability() {
        return probability;
    }
}
