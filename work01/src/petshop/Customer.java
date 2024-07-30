package petshop;

import java.time.LocalDate;

/**
 * 顾客类Customer
 *
 * 成员变量:
 * 顾客名字(String)
 * 到店次数(int)
 * 最新到店时间(LocalDate类)
 * 方法
 * 重写(@Override) toString() 方法, 要求按一定格式输出客户的所有信息
 */
public class Customer {
    private String name;
    private int times;
    private LocalDate latestTime;

    public Customer(String name) {
        this.name = name;
        times = 0;
        latestTime = LocalDate.now();
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", times=" + times +
                ", latestTime=" + latestTime +
                '}';
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public LocalDate getLatestTime() {
        return latestTime;
    }

    public void setLatestTime(LocalDate latestTime) {
        this.latestTime = latestTime;
    }
}
