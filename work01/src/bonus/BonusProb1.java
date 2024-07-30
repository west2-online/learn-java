package bonus;

public class BonusProb1 {
    public static void main(String[] args) {
        int[] arr1 = {1,3,5,7,9};
        int[] arr2 = {2,4,6,8,10};
        PrintArrays printArrays = new PrintArrays(arr1, arr2);

        Thread t1 = new Thread(printArrays::printArray1);
        Thread t2 = new Thread(printArrays::printArray2);

        t1.start();
        t2.start();
    }
}

class PrintArrays {
    private int[] arr1, arr2;
    private final Object lock = new Object();
    private boolean firstArrayTurn = true;

    public PrintArrays(int[] arr1, int[] arr2) {
        this.arr1 = arr1;
        this.arr2 = arr2;
    }

    public void printArray1() {
        synchronized (lock) {
            for (int num : arr1) {
                while (!firstArrayTurn) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.print(num + " ");
                firstArrayTurn = false;
                lock.notify();
            }
            lock.notify();
        }
    }

    public void printArray2() {
        synchronized (lock) {
            for (int num : arr2) {
                while (firstArrayTurn) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.print(num + " ");
                firstArrayTurn = true;
                lock.notify();
            }
            lock.notify();
        }
    }
}