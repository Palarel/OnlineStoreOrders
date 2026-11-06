package com.onlinestore.ui;

import com.onlinestore.exception.OrderRuleException;
import com.onlinestore.model.*;
import com.onlinestore.service.OrderService;

import java.io.IOException;
import java.util.Scanner;

public class ConsoleMenu {
    private final OrderService service;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleMenu(OrderService service) { this.service = service; }

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            try {
                int choice = readInt("Выберите действие: ");
                switch (choice) {
                    case 1 -> showProducts();
                    case 2 -> createOrder();
                    case 3 -> addProduct();
                    case 4 -> showOrders();
                    case 5 -> showOrder();
                    case 6 -> changeStatus();
                    case 7 -> cancelOrder();
                    case 8 -> removeProduct();
                    case 0 -> running = false;
                    default -> System.out.println("Нет такого пункта меню.");
                }
            } catch (OrderRuleException | IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            } finally {
                System.out.println();
            }
        }
        try {
            service.save();
            System.out.println("Данные сохранены. До свидания!");
        } catch (IOException e) {
            System.out.println("Не удалось сохранить данные: " + e.getMessage());
        }
    }

    private void printMenu() {
        System.out.println("=== ИНТЕРНЕТ-МАГАЗИН: ЗАКАЗЫ ===");
        System.out.println("1. Показать каталог товаров");
        System.out.println("2. Создать заказ");
        System.out.println("3. Добавить товар в заказ");
        System.out.println("4. Показать все заказы");
        System.out.println("5. Просмотреть заказ");
        System.out.println("6. Изменить статус заказа");
        System.out.println("7. Отменить заказ");
        System.out.println("8. Удалить товар из заказа");
        System.out.println("0. Выход");
    }

    private void showProducts() {
        System.out.println("--- Каталог ---");
        for (Product product : service.getProducts()) {
            System.out.printf("%s | Цена: %.2f | Доставка: %.2f%n",
                    product, product.getPrice(), product.getDeliveryCost());
        }
    }

    private void createOrder() {
        Order order = service.createOrder();
        System.out.println("Создан заказ №" + order.getId());
    }

    private void addProduct() throws OrderRuleException {
        int orderId = readInt("ID заказа: ");
        String productId = readText("ID товара: ");
        int quantity = readInt("Количество: ");
        service.addProduct(orderId, productId, quantity);
        System.out.println("Товар добавлен.");
    }

    private void showOrders() {
        if (service.getOrders().isEmpty()) {
            System.out.println("Заказов пока нет.");
            return;
        }
        for (Order order : service.getOrders()) {
            System.out.printf("Заказ №%d | %s | Сумма: %.2f | Позиций: %d%n",
                    order.getId(), order.getStatus(), order.calculateTotal(), order.getItems().size());
        }
    }

    private void showOrder() throws OrderRuleException {
        int id = readInt("ID заказа: ");
        Order order = service.findOrder(id)
                .orElseThrow(() -> new OrderRuleException("Заказ не найден."));
        System.out.println("Заказ №" + order.getId() + " | " + order.getStatus()
                + " | Создан: " + order.getCreatedAt());
        for (OrderItem item : order.getItems()) {
            System.out.printf("%s | Количество: %d | Сумма: %.2f | Доставка: %.2f%n",
                    item.getProduct().getName(), item.getQuantity(),
                    item.getSubtotal(), item.getDeliveryCost());
        }
        System.out.printf("ИТОГО: %.2f%n", order.calculateTotal());
    }

    private void changeStatus() throws OrderRuleException {
        int id = readInt("ID заказа: ");
        System.out.println("1. В обработке");
        System.out.println("2. Выполнен");
        int status = readInt("Новый статус: ");
        if (status == 1) service.changeStatus(id, OrderStatus.PROCESSING);
        else if (status == 2) service.changeStatus(id, OrderStatus.COMPLETED);
        else throw new IllegalArgumentException("Неверный вариант статуса.");
        System.out.println("Статус изменён.");
    }

    private void cancelOrder() throws OrderRuleException {
        service.cancelOrder(readInt("ID заказа для отмены: "));
        System.out.println("Заказ отменён.");
    }

    private void removeProduct() throws OrderRuleException {
        int orderId = readInt("ID заказа: ");
        String productId = readText("ID товара для удаления: ");
        service.removeProduct(orderId, productId);
        System.out.println("Товар удалён.");
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        String value = scanner.nextLine().trim();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Введите целое число.");
        }
    }

    private String readText(String prompt) {
        System.out.print(prompt);
        String value = scanner.nextLine().trim();
        if (value.isEmpty()) throw new IllegalArgumentException("Поле не может быть пустым.");
        return value;
    }
}
