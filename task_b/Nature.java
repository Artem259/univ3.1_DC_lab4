import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Nature implements Runnable {
    private final Garden garden;
    private final ReentrantReadWriteLock lock;

    public Nature(Garden garden, ReentrantReadWriteLock lock) {
        this.garden = garden;
        this.lock = lock;
    }

    @Override
    public void run() {
        while (true) {
            lock.writeLock().lock();
            try {
                garden.randomizedStates();
                System.out.println("+ Nature");
            } finally {
                lock.writeLock().unlock();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
