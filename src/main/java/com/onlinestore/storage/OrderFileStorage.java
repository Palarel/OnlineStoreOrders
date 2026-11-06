package com.onlinestore.storage;

import com.onlinestore.model.Order;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class OrderFileStorage {
    private final Path filePath;

    public OrderFileStorage(String fileName) {
        this.filePath = Path.of(fileName);
    }

    public void save(List<Order> orders) throws IOException {
        Path parent = filePath.getParent();
        if (parent != null) Files.createDirectories(parent);
        try (ObjectOutputStream output = new ObjectOutputStream(
                new BufferedOutputStream(Files.newOutputStream(filePath)))) {
            output.writeObject(new ArrayList<>(orders));
        }
    }

    @SuppressWarnings("unchecked")
    public List<Order> load() throws IOException, ClassNotFoundException {
        if (!Files.exists(filePath)) return new ArrayList<>();
        try (ObjectInputStream input = new ObjectInputStream(
                new BufferedInputStream(Files.newInputStream(filePath)))) {
            Object data = input.readObject();
            if (!(data instanceof List<?>)) {
                throw new IOException("Неверный формат файла заказов.");
            }
            return (List<Order>) data;
        }
    }
}
