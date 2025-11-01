package org.example;

public class ChinesePastoralDog extends Animal {

    private boolean isVaccineInjected;

    public ChinesePastoralDog(String name, int age, String gender, boolean isVaccineInjected) {
        super(name, age, gender, 100.0);
        this.isVaccineInjected = isVaccineInjected;
    }

    @Override
    public String toString() {
        return "中华田园犬{" +
                "名字='" + name + '\'' +
                ", 年龄=" + age +
                ", 性别='" + gender + '\'' +
                ", 价格=" + price +
                ", 是否注射狂犬疫苗=" + isVaccineInjected +
                '}';
    }
}

