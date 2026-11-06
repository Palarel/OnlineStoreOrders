package com.onlinestore.service;

import com.onlinestore.exception.OrderRuleException;
import com.onlinestore.model.*;
import com.onlinestore.repository.GenericRepository;
import com.onlinestore.repository.GenericSearch;
import com.onlinestore.storage.OrderFileStorage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class OrderService {
    private final GenericRepository<Order, Integer> orders = new GenericRepository<>();
    private final GenericRepository<Product, String> products = new GenericRepository<>();
    private final OrderFileStorage fileStorage;
    private int nextOrderId = 1;

    public OrderService(OrderFileStorage fileStorage) {
        this.fileStorage = fileStorage;
        seedProducts();
    }

    private void seedProducts() {
        products.add("P100", new PhysicalProduct("P100", "Наушники", 120.00, 8.00));
        products.add("P101", new PhysicalProduct("P101", "Клавиатура", 75.50, 6.50));
        products.add("D200", new DigitalProduct("D200", "Электронная книга", 15.00));
        products.add("D201", new DigitalProduct("D201", "Лицензия программы", 49.99));
    }

    public List<Product> getProducts() { return products.findAll(); }
    public List<Order> getOrders() { return orders.findAll(); }

    public Order createOrder() {
        Order order = new Order(nextOrderId++);
        orders.add(order.getId(), order);
        return order;
    }

    public Optional<Order> findOrder(int id) { return orders.findById(id); }

    public void addProduct(int orderId, String productId, int quantity) throws OrderRuleException {
        Order order = getRequiredOrder(orderId);
        Product product = products.findById(productId)
                .orElseThrow(() -> new OrderRuleException("Товар с таким ID не найден."));
        order.addItem(product, quantity);
    }

    public void addProduct(int orderId, String productId) throws OrderRuleException {
        addProduct(orderId, productId, 1);
    }

    public void removeProduct(int orderId, String productId) throws OrderRuleException {
        Order order = getRequiredOrder(orderId);
        if (!order.removeItem(productId)) {
            throw new OrderRuleException("Такого товара нет в заказе.");
        }
    }

    public void changeStatus(int orderId, OrderStatus status) throws OrderRuleException {
        getRequiredOrder(orderId).changeStatus(status);
    }

    public void cancelOrder(int orderId) throws OrderRuleException {
        getRequiredOrder(orderId).cancel();
    }

    private Order getRequiredOrder(int id) throws OrderRuleException {
        return orders.findById(id)
                .orElseThrow(() -> new OrderRuleException("Заказ с таким ID не найден."));
    }

    public Optional<Order> findOrderByStatus(OrderStatus status) {
        return GenericSearch.findFirst(orders.values(), order -> order.getStatus() == status);
    }

    public void save() throws IOException {
        fileStorage.save(orders.findAll());
    }

    public void load() throws IOException, ClassNotFoundException {
        List<Order> loaded = fileStorage.load();
        for (Order order : loaded) {
            orders.add(order.getId(), order);
            nextOrderId = Math.max(nextOrderId, order.getId() + 1);
        }
    }
}
