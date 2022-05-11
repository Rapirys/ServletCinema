package com.servlet.cinema.framework.Util;


/**
 * A tuple of things.
 *
 * @param <S> Type of the first thing.
 * @param <T> Type of the second thing.
 */
public final class Pair<S, T> {
    private final S first;
    private final T second;

    public Pair(S first, T second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public static <S, T> Pair<S, T> of(S first, T second) {
        return new Pair<S, T>(first, second);
    }

    public S getFirst() {
        return this.first;
    }

    public T getSecond() {
        return this.second;
    }

    public String toString() {
        return String.format("%s->%s", this.first, this.second);
    }
}