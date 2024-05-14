package com.saphirus.utils;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomCollection<E> {
    public final NavigableMap<Double, E> map;

    private final Random random;

    private double total;

    public RandomCollection() {
        this(new Random());
    }

    public RandomCollection(Random random) {
        this.map = new TreeMap<>();
        this.total = 0.0D;
        this.random = random;
    }

    public void add(double weight, E result) {
        if (weight <= 0.0D)
            return;
        this.total += weight;
        this.map.put(Double.valueOf(this.total), result);
    }

    public WinningObject<E> next() {
        double value = this.random.nextDouble() * this.total;
        return new WinningObject<>((E)this.map.ceilingEntry(Double.valueOf(value)).getValue(), value);
    }

    public RandomCollection<E> clone() {
        try {
            return (RandomCollection<E>)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void clear() {
        this.map.clear();
        this.total = 0.0D;
    }

    public static class WinningObject<E> {
        public E entry;

        public double ticket;

        public WinningObject(E e, double d) {
            this.entry = e;
            this.ticket = d;
        }
    }
}
