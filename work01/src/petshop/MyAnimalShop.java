package petshop;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MyAnimalShop implements AnimalShop{
    private double money;
    private List<Animal> animalList;
    private List<Customer> customerList;
    private boolean launched;
    private LocalTime endOfWork = LocalTime.of(17, 0); // 定义下班时间，下午5点

    public MyAnimalShop(double money) {
        this.money = money;
        animalList = new ArrayList<>();
        customerList = new ArrayList<>();
        launched = true;
    }

    public MyAnimalShop(double money, List<Animal> animalList) {
        this.money = money;
        this.animalList = animalList;
        customerList = new ArrayList<>();
        launched = true;
    }

    /**
     *
     * @param animal 买入的动物
     * 买入动物 -> 买入一只动物，记得在动物列表中添加，
     * 如余额不足则抛出异常InsufficientBalanceException
     */
    @Override
    public void buy(Animal animal) {
        if (money >= animal.getPrice()) {
            money -= animal.getPrice();
            animalList.add(animal);
        } else {
            throw new InsufficientBalanceException("余额不足！");
        }
    }

    /**
     *
     * @param customer 顾客
     * @param animal 想要的动物
     * 招待客户 -> 接受客户参数，在顾客列表中加入新顾客，
     * 出售动物，如果店内没有动物，抛出AnimalNotFoundException。
     * 通过toString输出动物信息，并把钱入账，将动物移除列表
     */
    @Override
    public void serveCustomer(Customer customer, Animal animal) {
        if (!animalList.contains(animal)) {
            throw new AnimalNotFountException("未找到动物！");
        }
        customerList.add(customer);
        customer.setTimes(customer.getTimes() + 1);
        customer.setLatestTime(LocalDate.now());
        System.out.println(animal);
        money += animal.getPrice();
        animalList.remove(animal);
    }

    /**
     * 歇业 -> (LocalDate判断) 输出当天光顾的客户的列表信息，计算并输出一天的利润
     */
    @Override
    public void close(LocalTime currentTime) {
        // 判断是否到下班时间
        if (currentTime.isAfter(endOfWork) || currentTime.equals(endOfWork)) {
            for (Customer customer : customerList) {
                System.out.println(customer);
            }
            System.out.println("今日总利润为：" + money);
            launched = false;
        }
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public List<Animal> getAnimalList() {
        return animalList;
    }

    public void setAnimalList(List<Animal> animalList) {
        this.animalList = animalList;
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }

    public void setLaunched(boolean launched) {
        this.launched = launched;
    }

    public boolean isLaunched() {
        return launched;
    }

    public LocalTime getEndOfWork() {
        return endOfWork;
    }

    public void setEndOfWork(LocalTime endOfWork) {
        this.endOfWork = endOfWork;
    }
}
