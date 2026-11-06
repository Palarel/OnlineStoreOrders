package com.onlinestore.model;

public class DigitalProduct extends Product {
    private static final long serialVersionUID = 1L;

    public DigitalProduct(String id, String name, double price) {
        super(id, name, price);
    }

    @Override
    public double getDeliveryCost() { return 0; }

    @Override
    public String getType() { return "Цифровой"; }
}
