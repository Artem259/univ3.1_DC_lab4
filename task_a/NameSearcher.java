import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class NameSearcher implements Runnable {
    private final StringList phones;
    private final String path;
    private final MytReadWriteLock lock;

    public NameSearcher(StringList phones, String path, MytReadWriteLock lock) {
        this.phones = phones;
        this.path = path;
        this.lock = lock;
    }

    @Override
    public void run() {
        while (true) {
            lock.lockReading();
            boolean flag = false;
            try {
                String phone = phones.getRandom();
                File file = new File(path);
                BufferedReader reader = new BufferedReader(new FileReader(file));

                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    if (currentLine.equals(""))
                        continue;
                    String[] strArr = currentLine.split(" ");
                    if (Objects.equals(strArr[1], phone)) {
                        System.out.println(" N " + phone + " >> " + strArr[0]);
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    System.out.println(" N " + phone + " >> #");
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
