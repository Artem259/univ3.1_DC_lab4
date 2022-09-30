import java.util.Random;

public class Garden {
    private final int[][] garden;
    private final int rows;
    private final int columns;
    private final Random random;

    public Garden(int rows, int columns) {
        random = new Random();
        this.rows = rows;
        this.columns = columns;
        garden = new int[rows][columns];
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                garden[i][j] = 1;
            }
        }
    }

    public void randomizedStates() {
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                garden[i][j] = random.nextInt(2);
            }
        }
    }

    public void goodStates() {
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                garden[i][j] = 1;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                result.append(garden[i][j]).append(" ");
            }
            if (i != rows-1)
                result.append("\n");
        }
        return result.toString();
    }
}
