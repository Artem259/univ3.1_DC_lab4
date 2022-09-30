import java.io.*;
import java.util.Random;

public class RecordRemover implements Runnable {
    private final String path;
    private final MytReadWriteLock lock;
    private static Random random;

    public RecordRemover(String path, MytReadWriteLock lock) {
        this.path = path;
        this.lock = lock;
        random = new Random();
    }

    @Override
    public void run() {
        while (true) {
            lock.lockWriting();
            try {
                int lines = linesInFile();
                if (lines > 5) {
                    int lineToRemoveIndex = random.nextInt(lines-1);
                    File inputFile = new File(path);
                    File tempFile = new File("temp.txt");

                    BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                    String currentLine;
                    int currentLineIndex = 0;
                    while ((currentLine = reader.readLine()) != null) {
                        if (currentLineIndex == lineToRemoveIndex) {
                            System.out.println(" - " + currentLine);
                            currentLineIndex++;
                            continue;
                        }
                        writer.write(currentLine + "\n");
                        currentLineIndex++;
                    }
                    writer.close();
                    reader.close();
                    if (!inputFile.delete())
                        throw new RuntimeException();
                    if (!tempFile.renameTo(inputFile))
                        throw new RuntimeException();
                }
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

    private int linesInFile() {
        int lines = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            while (reader.readLine() != null) lines++;
            reader.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }
}
