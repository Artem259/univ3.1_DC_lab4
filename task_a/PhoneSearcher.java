import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class PhoneSearcher implements Runnable {
    private final StringList names;
    private final String path;
    private final MytReadWriteLock lock;

    public PhoneSearcher(StringList names, String path, MytReadWriteLock lock) {
        this.names = names;
        this.path = path;
        this.lock = lock;
    }

    @Override
    public void run() {
        while (true) {
            lock.lockReading();
            boolean flag = false;
            try {
                String name = names.getRandom();
                File file = new File(path);
                BufferedReader reader = new BufferedReader(new FileReader(file));

                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    if (currentLine.equals(""))
                        continue;
                    String[] strArr = currentLine.split(" ");
                    if (Objects.equals(strArr[0], name)) {
                        System.out.println(" P " + name + " >> " + strArr[1]);
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    System.out.println(" P " + name + " >> #");
                }
                reader.close();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            finally {
                lock.unlockReading();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
