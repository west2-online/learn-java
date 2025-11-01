package org.example;

public class Cat extends Animal {
    public Cat(String name, int age, String gender) {
        super(name, age, gender, 200.0);
    }

    @Override
    public String toString() {
        return "猫猫{" +
                "名字='" + name + '\'' +
                ", 年龄=" + age +
                ", 性别='" + gender + '\'' +
                ", 价格=" + price +
                '}';
    }
}

