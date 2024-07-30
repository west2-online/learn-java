package petshop;

public class AnimalNotFountException extends RuntimeException{
    public AnimalNotFountException(){}
    public AnimalNotFountException(String message) {
        super(message);
    }
}
