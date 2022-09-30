import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MonitorFile implements Runnable {
    private final Garden garden;
    private final String path;
    private final ReentrantReadWriteLock lock;

    public MonitorFile(Garden garden, String path, ReentrantReadWriteLock lock) {
        this.garden = garden;
        this.path = path;
        this.lock = lock;
    }

    @Override
    public void run() {
        new File(path).delete();
        while (true) {
            lock.readLock().lock();
            try {
                FileWriter fw = new FileWriter(path, true);
                fw.write(garden.toString() + "\n\n");
                System.out.println("> MonitorFile");
                fw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
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
