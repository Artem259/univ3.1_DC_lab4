import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MonitorCmd implements Runnable {
    private final Garden garden;
    private final ReentrantReadWriteLock lock;

    public MonitorCmd(Garden garden, ReentrantReadWriteLock lock) {
        this.garden = garden;
        this.lock = lock;
    }

    @Override
    public void run() {
        while (true) {
            lock.readLock().lock();
            try {
                System.out.println("-----");
                System.out.println(garden);
                System.out.println("-----");
            } finally {
                lock.readLock().unlock();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
