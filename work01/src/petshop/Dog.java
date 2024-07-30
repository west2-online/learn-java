package petshop;

/**
 * 中华田园犬类
 * 变量: isVaccineInjected(boolean 是否注射狂犬病疫苗)
 * 价格100元
 */
public class Dog extends Animal{

    public boolean isVaccineInjected;

    public boolean isVaccineInjected() {
        return isVaccineInjected;
    }

    public void setVaccineInjected(boolean vaccineInjected) {
        isVaccineInjected = vaccineInjected;
    }

    public Dog(String name, int age, String sex) {
        super(name, age, sex, 100);
    }
}
