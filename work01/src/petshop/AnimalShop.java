package petshop;

import java.time.LocalTime;

public interface AnimalShop {
    public void buy(Animal animal);
    public void serveCustomer(Customer customer, Animal animal);
    public void close(LocalTime currentTime);
}
