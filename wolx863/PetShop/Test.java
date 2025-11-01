package org.example;

import java.time.LocalDate;

public class Test {
    public static void main(String[] args) {
        MyAnimalShop shop = new MyAnimalShop(1000.0);

        try {
            Animal dog = new ChinesePastoralDog("旺财", 2, "公", true);
            Animal cat = new Cat("咪咪", 1, "母");
            shop.buyAnimal(dog);
            shop.buyAnimal(cat);

            Customer customer1 = new Customer("A", 1, LocalDate.now());
            shop.serveCustomer(customer1);

            Customer customer2 = new Customer("B", 2, LocalDate.now());
            shop.serveCustomer(customer2);

        } catch (InsufficientBalanceException e) {
            System.out.println("买入失败：" + e.getMessage());
        } catch (AnimalNotFoundException e) {
            System.out.println("招待失败：" + e.getMessage());
        } finally {
            shop.closeShop();
        }
    }
}