package org.example;

    public interface AnimalShop {
        void buyAnimal(Animal animal) throws InsufficientBalanceException;

        void serveCustomer(Customer customer) throws AnimalNotFoundException;

        void closeShop();
    }

