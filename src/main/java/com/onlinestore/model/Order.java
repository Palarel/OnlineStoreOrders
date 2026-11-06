package com.onlinestore.model;

import com.onlinestore.exception.OrderRuleException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int id;
    private final LocalDateTime createdAt;
    private final List<OrderItem> items = new ArrayList<>();
    private OrderStatus status;

    public Order(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID заказа должен быть положительным.");
        this.id = id;
        this.createdAt = LocalDateTime.now();
        this.status = OrderStatus.NEW;
    }

    public int getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public OrderStatus getStatus() { return status; }
    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }

    public void addItem(Product product, int quantity) throws OrderRuleException {
        if (status != OrderStatus.NEW) {
            throw new OrderRuleException("Добавлять товары можно только в новый заказ.");
        }
        items.add(new OrderItem(product, quantity));
    }

    public void addItem(Product product) throws OrderRuleException {
        addItem(product, 1);
    }

    public boolean removeItem(String productId) throws OrderRuleException {
        if (status != OrderStatus.NEW) {
            throw new OrderRuleException("Изменять можно только новый заказ.");
        }
        return items.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public double calculateTotal() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getSubtotal() + item.getDeliveryCost();
        }
        return total;
    }

    public void changeStatus(OrderStatus newStatus) throws OrderRuleException {
        if (newStatus == null) throw new OrderRuleException("Статус не указан.");
        if (status == OrderStatus.CANCELLED || status == OrderStatus.COMPLETED) {
            throw new OrderRuleException("Нельзя изменить статус закрытого заказа.");
        }
        if (newStatus == OrderStatus.NEW || newStatus == OrderStatus.CANCELLED) {
            throw new OrderRuleException("Для отмены используйте отдельную операцию.");
        }
        if (newStatus == OrderStatus.COMPLETED && items.isEmpty()) {
            throw new OrderRuleException("Нельзя выполнить пустой заказ.");
        }
        status = newStatus;
    }

    public void cancel() throws OrderRuleException {
        if (status == OrderStatus.COMPLETED) {
            throw new OrderRuleException("Нельзя отменить уже выполненный заказ.");
        }
        if (status == OrderStatus.CANCELLED) {
            throw new OrderRuleException("Заказ уже отменён.");
        }
        status = OrderStatus.CANCELLED;
    }
}
