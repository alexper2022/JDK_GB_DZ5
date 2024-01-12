import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Philosopher extends Thread {
    private final String name;
    private final int lFork;
    private final int rFork;
    private final CountDownLatch cdl;
    private final Table table;
    private final int COUNTS_OF_VISITS = 3;
    private int countEat;

    public Philosopher(String name, Table table, int lFork, int rFork, CountDownLatch cdl) {
        this.table = table;
        this.name = name;
        this.lFork = lFork;
        this.rFork = rFork;
        this.cdl = cdl;
        countEat = 0;
    }

    private void eat() throws InterruptedException {
        if (table.takeForks(lFork, rFork)) {
            System.out.printf("%s кушает вилками %s и %s.\n", name, lFork, rFork);
            sleep(new Random().nextLong(2000, 5000));
            table.dropTheForks(lFork, rFork);
            System.out.printf("%s поел, положил вилки %s и %s и размышляет.\n", name, lFork, rFork);
            countEat++;
        }
    }

    private void toThink() throws InterruptedException {
        sleep(new Random().nextLong(500, 1500));
    }

    @Override
    public void run() {
        while (countEat < COUNTS_OF_VISITS) {
            try {
                toThink();
                eat();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        cdl.countDown();
        System.out.printf("! %s покушал %s раз(а)!\n", name, COUNTS_OF_VISITS);
    }
}