package com.onlinestore.repository;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

public final class GenericSearch {
    private GenericSearch() { }

    public static <T> Optional<T> findFirst(Collection<T> items, Predicate<T> condition) {
        for (T item : items) {
            if (condition.test(item)) return Optional.of(item);
        }
        return Optional.empty();
    }
}
