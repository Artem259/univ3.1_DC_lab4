import java.io.FileWriter;
import java.io.IOException;

public class RecordInserter implements Runnable {
    private final StringList names;
    private final StringList phones;
    private final String path;
    private final MytReadWriteLock lock;

    public RecordInserter(StringList names, StringList phones, String path, MytReadWriteLock lock) {
        this.names = names;
        this.phones = phones;
        this.path = path;
        this.lock = lock;
    }

    @Override
    public void run() {
        while (true) {
            lock.lockWriting();
            try {
                String name = names.getRandom();
                String phone = phones.getRandom();
                FileWriter fw = new FileWriter(path, true);
                fw.write(name + " - " + phone + "\n");
                System.out.println(" + " + name + " " + phone + " " + Thread.currentThread().getName());
                fw.close();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            finally {
                lock.unlockWriting();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
