package petshop;

/**
 * 一个Animal动物类 (抽象类 abstract )
 *
 * 变量:
 * 动物名(String)
 * 年龄(int)
 * 性别
 * 价格(double)
 * ....
 * 方法
 * 一个全参构造方法
 * 一个抽象的toString() 方法
 * ........
 */
public abstract class Animal {
    protected String name;
    protected int age;
    protected String sex;
    protected double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Animal(String name, int age, String sex, double price) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", price=" + price +
                '}';
    }
}
