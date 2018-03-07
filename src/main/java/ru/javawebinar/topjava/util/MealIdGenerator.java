package ru.javawebinar.topjava.util;

import java.util.concurrent.atomic.AtomicInteger;

public class MealIdGenerator {
    private final static AtomicInteger id = new AtomicInteger();

    public static int getId() {
        return id.incrementAndGet();
    }
}