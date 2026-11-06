package com.onlinestore.model;

public class PhysicalProduct extends Product {
    private static final long serialVersionUID = 1L;
    private double deliveryCost;

    public PhysicalProduct(String id, String name, double price, double deliveryCost) {
        super(id, name, price);
        setDeliveryCost(deliveryCost);
    }

    public double getDeliveryCost() { return deliveryCost; }

    public void setDeliveryCost(double deliveryCost) {
        if (!Double.isFinite(deliveryCost) || deliveryCost < 0) {
            throw new IllegalArgumentException("Стоимость доставки не может быть отрицательной.");
        }
        this.deliveryCost = deliveryCost;
    }

    @Override
    public String getType() { return "Физический"; }
}
