package com.onlinestore.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GenericRepository<T, ID> {
    private final Map<ID, T> storage = new LinkedHashMap<>();

    public void add(ID id, T item) {
        if (id == null || item == null) throw new IllegalArgumentException("ID и объект обязательны.");
        if (storage.containsKey(id)) throw new IllegalArgumentException("Объект с таким ID уже существует.");
        storage.put(id, item);
    }

    public Optional<T> findById(ID id) {
        return Optional.ofNullable(storage.get(id));
    }

    public boolean remove(ID id) {
        return storage.remove(id) != null;
    }

    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Collection<T> values() {
        return new ArrayList<>(storage.values());
    }

    public int size() { return storage.size(); }
}
