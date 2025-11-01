package org.example;

public class Fox extends Animal {
    public Fox(String name, int age, String gender) {
        super(name, age, gender, 500.0);
    }

    @Override
    public String toString() {
        return "狐狸{" +
                "名字='" + name + '\'' +
                ", 年龄=" + age +
                ", 性别='" + gender + '\'' +
                ", 价格=" + price +
                '}';
    }
}

