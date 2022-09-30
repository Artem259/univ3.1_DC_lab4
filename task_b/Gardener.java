import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Gardener implements Runnable {
    private final Garden garden;
    private final ReentrantReadWriteLock lock;

    public Gardener(Garden garden, ReentrantReadWriteLock lock) {
        this.garden = garden;
        this.lock = lock;
    }

    @Override
    public void run() {
        while (true) {
            lock.writeLock().lock();
            try {
                garden.goodStates();
                System.out.println("+ Gardener");
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
