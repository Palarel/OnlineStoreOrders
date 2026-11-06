package com.onlinestore;

import com.onlinestore.service.OrderService;
import com.onlinestore.storage.OrderFileStorage;
import com.onlinestore.ui.ConsoleMenu;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        OrderService service = new OrderService(new OrderFileStorage("data/orders.dat"));
        try {
            service.load();
            System.out.println("Сохранённые заказы загружены.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Не удалось загрузить заказы: " + e.getMessage());
        } finally {
            System.out.println("Приложение интернет-магазина запущено.");
        }
        new ConsoleMenu(service).start();
    }
}
