# Online Store Orders

Консольное Java-приложение для оформления и обработки заказов интернет-магазина (индивидуальный вариант 1).

## Возможности
- каталог физических и цифровых товаров;
- создание заказа и добавление товаров с проверкой количества;
- расчёт стоимости с учётом доставки: доставка начисляется только для физических товаров;
- поиск заказа по ID, просмотр списка заказов и состава заказа;
- изменение статуса заказа и отмена заказа (выполненный заказ отменить нельзя);
- автоматическое сохранение и восстановление заказов из `data/orders.dat`;
- обработка ошибок предметной области и некорректного ввода.

## Требования
- JDK 17 или новее;
- Git.

## Запуск
Из корневого каталога проекта:

```bash
javac -encoding UTF-8 -d out $(find src/main/java -name "*.java")
java -cp out com.onlinestore.Main
```

В Windows PowerShell:

```powershell
Get-ChildItem -Recurse src/main/java -Filter *.java | ForEach-Object { $_.FullName } | Set-Content sources.txt
javac -encoding UTF-8 -d out @sources.txt
java -cp out com.onlinestore.Main
```

Файл `data/orders.dat` создаётся автоматически после первого сохранения. Это бинарный файл Java Object Serialization.

## Тестирование
Linux/macOS:

```bash
javac -encoding UTF-8 -d out $(find src/main/java src/test/java -name "*.java")
java -ea -cp out com.onlinestore.OrderServiceTest
```

Windows PowerShell:

```powershell
Get-ChildItem -Recurse src/main/java,src/test/java -Filter *.java | ForEach-Object { $_.FullName } | Set-Content sources.txt
javac -encoding UTF-8 -d out @sources.txt
java -ea -cp out com.onlinestore.OrderServiceTest
```

## Архитектура
- `model` — предметная область: абстрактный `Product`, наследники `PhysicalProduct` и `DigitalProduct`, `Order`, `OrderItem`, статусы.
- `repository` — обобщённое хранилище `GenericRepository<T, ID>`.
- `service` — бизнес-логика заказов и расчёт стоимости.
- `storage` — файловое сохранение и загрузка.
- `exception` — собственные исключения.
- `ui` — консольное меню и ввод пользователя.

## ООП и требования задания
Инкапсуляция обеспечивается закрытыми полями и проверками в конструкторах/сеттерах. Наследование и абстракция реализованы через `Product` и два класса товаров. Полиморфизм: `getDeliveryCost()` переопределён в наследниках; `addItem()` перегружен. Интерфейс `PriceCalculable` задаёт расчёт стоимости. Коллекции `ArrayList` и `LinkedHashMap`, generics в репозитории и `GenericSearch`, собственное исключение `OrderRuleException`, обработка `try/catch/finally`, `throw/throws`, файловый ввод-вывод.
