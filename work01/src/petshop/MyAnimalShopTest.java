package petshop;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MyAnimalShopTest {

    @Test
    public void testConstruct() {
        List<Animal> animalList = new ArrayList<>();
        animalList.add(new Dog("dog1", 1, SEX.MALE));
        animalList.add(new Cat("cat1", 2, SEX.FEMALE));
        MyAnimalShop myAnimalShop = new MyAnimalShop(10000, animalList);
        assertTrue(myAnimalShop.getCustomerList().isEmpty());
        assertFalse(myAnimalShop.getAnimalList().isEmpty());
    }

    @Test(expected = InsufficientBalanceException.class)
    public void testBuyError() {
        MyAnimalShop myAnimalShop = new MyAnimalShop(0);
        myAnimalShop.setAnimalList(new ArrayList<>());
        Dog dog = new Dog("1", 1, SEX.FEMALE);
        myAnimalShop.getAnimalList().add(dog);
        myAnimalShop.buy(dog);
    }

    @Test
    public void testBuy() {
        double money = 1000;
        MyAnimalShop myAnimalShop = new MyAnimalShop(money);
        myAnimalShop.setAnimalList(new ArrayList<>());
        Dog dog = new Dog("1", 1, SEX.FEMALE);
        myAnimalShop.getAnimalList().add(dog);
        myAnimalShop.buy(dog);
        assertTrue(myAnimalShop.getMoney() == money - dog.getPrice());
    }

    @Test(expected = AnimalNotFountException.class)
    public void testServeCustomerError() {
        MyAnimalShop myAnimalShop = new MyAnimalShop(0);
        myAnimalShop.setAnimalList(new ArrayList<>());
        Dog dog = new Dog("1", 1, SEX.FEMALE);
        myAnimalShop.serveCustomer(new Customer("1"), dog);
    }

    @Test
    public void testServeCustomer() {
        double money = 0;
        MyAnimalShop myAnimalShop = new MyAnimalShop(money);
        myAnimalShop.setAnimalList(new ArrayList<>());
        Dog dog = new Dog("1", 1, SEX.FEMALE);
        myAnimalShop.getAnimalList().add(dog);
        myAnimalShop.serveCustomer(new Customer("1"), dog);
        assertTrue(myAnimalShop.getMoney() == money + dog.getPrice());
    }

    @Test
    public void testClose() {
        double money = 1000;
        MyAnimalShop myAnimalShop = new MyAnimalShop(money);
        List<Customer> customerList = new ArrayList<>();
        myAnimalShop.setCustomerList(customerList);
        // 测试输出语句是否正确
        int testLen = 5;
        String outContents = "";
        for (int i = 0; i < testLen; i++) {
            Customer customer = new Customer(String.valueOf(i));
            customer.setTimes(i);
            customer.setLatestTime(LocalDate.now());
            customerList.add(customer);
            outContents += customer.toString() + "\r\n";
        }
        outContents += new String("今日总利润为：" + money + "\r\n");

        // 输出流重定向
        // 创建一个 ByteArrayOutputStream 来捕获输出
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // 调用打印消息的方法
            // 已达到下班时间
            LocalTime endOfWork = LocalTime.of(17, 0);
            myAnimalShop.close(endOfWork);
        } finally {
            // 恢复原始的 System.out
            System.setOut(originalOut);
        }

        // 断言输出内容是否与预期一致
        assertEquals(outContents, outputStream.toString());
    }
}
