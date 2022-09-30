import java.io.File;

public class Application {
    private final int recordInserters;
    private final int recordRemovers;
    private final int nameSearchers;
    private final int phoneSearchers;

    public Application(int recordInserters, int recordRemovers, int nameSearchers, int phoneSearchers) {
        this.recordInserters = recordInserters;
        this.recordRemovers = recordRemovers;
        this.nameSearchers = nameSearchers;
        this.phoneSearchers = phoneSearchers;
    }

    public void start() {
        MytReadWriteLock lock = new MytReadWriteLock();
        StringList names = new StringList("files/names.txt");
        StringList phones = new StringList("files/phones.txt");
        new File("files/database.txt").delete();

        for(int i=0; i<recordInserters; i++) {
            new Thread(new RecordInserter(names, phones, "files/database.txt", lock)).start();
        }
        for(int i=0; i<recordRemovers; i++) {
            new Thread(new RecordRemover("files/database.txt", lock)).start();
        }
        for(int i=0; i<nameSearchers; i++) {
            new Thread(new NameSearcher(phones, "files/database.txt", lock)).start();
        }
        for(int i=0; i<phoneSearchers; i++) {
            new Thread(new PhoneSearcher(names, "files/database.txt", lock)).start();
        }
    }

    public static void main(String[] args) {
        new Application(2,2,2,2).start();
    }
}
