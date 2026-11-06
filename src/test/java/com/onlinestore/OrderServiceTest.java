package com.onlinestore;

import com.onlinestore.exception.OrderRuleException;
import com.onlinestore.model.*;
import com.onlinestore.service.OrderService;
import com.onlinestore.storage.OrderFileStorage;

import java.nio.file.Files;
import java.nio.file.Path;

public class OrderServiceTest {
    public static void main(String[] args) throws Exception {
        Path tempFile = Path.of("test-orders.dat");
        Files.deleteIfExists(tempFile);
        OrderService service = new OrderService(new OrderFileStorage(tempFile.toString()));

        Order order = service.createOrder();
        service.addProduct(order.getId(), "P100", 2);
        service.addProduct(order.getId(), "D200", 1);
        check(Math.abs(order.calculateTotal() - 263.00) < 0.001,
                "Сумма должна включать доставку физического товара");

        boolean invalidQuantity = false;
        try {
            service.addProduct(order.getId(), "P101", 0);
        } catch (IllegalArgumentException e) {
            invalidQuantity = true;
        }
        check(invalidQuantity, "Нулевое количество должно отклоняться");

        service.changeStatus(order.getId(), OrderStatus.PROCESSING);
        service.changeStatus(order.getId(), OrderStatus.COMPLETED);
        boolean completedCancelBlocked = false;
        try {
            service.cancelOrder(order.getId());
        } catch (OrderRuleException e) {
            completedCancelBlocked = true;
        }
        check(completedCancelBlocked, "Выполненный заказ нельзя отменить");

        service.save();
        OrderService restored = new OrderService(new OrderFileStorage(tempFile.toString()));
        restored.load();
        check(restored.findOrder(order.getId()).isPresent(), "Заказ должен восстановиться из файла");
        check(restored.findOrder(order.getId()).orElseThrow().getStatus() == OrderStatus.COMPLETED,
                "Статус должен сохраниться");

        Files.deleteIfExists(tempFile);
        System.out.println("Все тесты пройдены.");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
