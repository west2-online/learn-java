package org.example;

public abstract class Animal {
    protected String name;
    protected int age;
    protected String gender;
    protected double price;

    public Animal(String name, int age, String gender, double price) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.price = price;
    }

    public abstract String toString();

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

