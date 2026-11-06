package com.onlinestore.model;

import java.io.Serializable;

public abstract class Product implements PriceCalculable, Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String name;
    private double price;

    protected Product(String id, String name, double price) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID товара не может быть пустым.");
        }
        setName(name);
        setPrice(price);
        this.id = id;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Название товара не может быть пустым.");
        }
        this.name = name.trim();
    }

    public void setPrice(double price) {
        if (!Double.isFinite(price) || price < 0) {
            throw new IllegalArgumentException("Цена должна быть неотрицательным числом.");
        }
        this.price = price;
    }

    @Override
    public double calculatePrice(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество должно быть больше нуля.");
        }
        return price * quantity;
    }

    public abstract double getDeliveryCost();

    public abstract String getType();

    @Override
    public String toString() {
        return id + " | " + name + " | " + price + " | " + getType();
    }
}
