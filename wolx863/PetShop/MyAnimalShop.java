package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MyAnimalShop implements AnimalShop {
    private double balance;
    private final List<Animal> animalList;
    private final List<Customer> customerList;
    private boolean isOpen;
    private double dailyProfit;

    public MyAnimalShop(double initialBalance) {
        this.balance = initialBalance;
        this.animalList = new ArrayList<>();
        this.customerList = new ArrayList<>();
        this.isOpen = true;
        this.dailyProfit = 0.0;
    }

    @Override
    public void buyAnimal(Animal animal) throws InsufficientBalanceException {
        if (!isOpen) {
            System.out.println("宠物店已歇业，无法买入动物");
            return;
        }
        if (balance < animal.getPrice()) {
            throw new InsufficientBalanceException("余额不足，无法买入" + animal.getName());
        }
        balance -= animal.getPrice();
        animalList.add(animal);
        System.out.println("成功买入：" + animal);
    }

    @Override
    public void serveCustomer(Customer customer) throws AnimalNotFoundException {
        if (!isOpen) {
            System.out.println("宠物店已歇业，无法招待客户");
            return;
        }
        customerList.add(customer);
        if (animalList.isEmpty()) {
            throw new AnimalNotFoundException("店内暂无动物可出售");
        }
        Animal soldAnimal = animalList.removeFirst();
        balance += soldAnimal.getPrice();
        dailyProfit += soldAnimal.getPrice();
        System.out.println("出售动物：" + soldAnimal);
        System.out.println("成功招待客户：" + customer.getName());
    }

    @Override
    public void closeShop() {
        isOpen = false;
        System.out.println("=====今日歇业=====");
        System.out.println("今日光顾的客户：");
        LocalDate today = LocalDate.now();
        for (Customer customer : customerList) {
            if (customer.getLatestVisitDate().equals(today)) {
                System.out.println(customer);
            }
        }
        System.out.println("今日利润：" + dailyProfit + "元");
        System.out.println("=================");
    }
}
