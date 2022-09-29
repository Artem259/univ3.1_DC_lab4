import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class StringList {
    private final ArrayList<String> list;
    private static Random random;

    public StringList() {
        this.list = new ArrayList<>();
        random = new Random();
    }

    public StringList(ArrayList<String> list) {
        this.list = list;
        random = new Random();
    }

    public StringList(Path path) {
        this.list = new ArrayList<>();
        random = new Random();

        Scanner sc;
        try {
            sc = new Scanner(path);
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                list.add(line);
            }
            sc.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void add(String s) {
        list.add(s);
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public String getRandom() {
        return list.get(random.nextInt(list.size()));
    }
}
