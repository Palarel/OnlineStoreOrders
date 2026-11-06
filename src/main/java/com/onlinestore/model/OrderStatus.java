package com.onlinestore.model;

public enum OrderStatus {
    NEW("Новый"),
    PROCESSING("В обработке"),
    COMPLETED("Выполнен"),
    CANCELLED("Отменён");

    private final String title;

    OrderStatus(String title) { this.title = title; }

    @Override
    public String toString() { return title; }
}
