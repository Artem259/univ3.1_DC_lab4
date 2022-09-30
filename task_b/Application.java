import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Application {
    private final Garden garden;

    public Application(Garden garden) {
        this.garden = garden;
    }

    public void start() {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        new Thread(new Gardener(garden, lock)).start();
        new Thread(new Nature(garden, lock)).start();
        new Thread(new MonitorCmd(garden, lock)).start();
        new Thread(new MonitorFile(garden, "output.txt", lock)).start();
    }

    public static void main(String[] args) {
        new Application(new Garden(5,5)).start();
    }
}
