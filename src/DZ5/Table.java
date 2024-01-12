import java.util.concurrent.CountDownLatch;

public class Table extends Thread {
    private final int PHILOSOPHERS = 5;
    private final Fork[] forks;
    private final Philosopher[] philosophers;
    private final CountDownLatch cdl;

    public Table() {
        forks = new Fork[PHILOSOPHERS];
        philosophers = new Philosopher[PHILOSOPHERS];
        cdl = new CountDownLatch(PHILOSOPHERS);
        init();
    }

    @Override
    public void run() {
        System.out.println("Начинаем!\n");
        try {
            thinking();
            cdl.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("\nВсе философы поели!");
    }

    public synchronized boolean takeForks(int lFork, int rFork) {
        if (!forks[lFork].getUsing() && !forks[rFork].getUsing()) {
            forks[lFork].setUsing(true);
            forks[rFork].setUsing(true);
            return true;
        }
        return false;
    }

    public void dropTheForks(int lFork, int rFork) {
        forks[lFork].setUsing(false);
        forks[rFork].setUsing(false);
    }

    private void init() {
        for (int i = 0; i < PHILOSOPHERS; i++) {
            forks[i] = new Fork();
        }

        for (int i = 0; i < PHILOSOPHERS; i++) {
            philosophers[i] = new Philosopher("Философ " + i, this, i, (i + 1) % PHILOSOPHERS, cdl);
        }
    }

    private void thinking() {
        for (Philosopher philosopher : philosophers) {
            philosopher.start();
        }
    }
}
