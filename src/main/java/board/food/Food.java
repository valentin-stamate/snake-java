package board.food;

import board.CellType;

import java.util.ArrayList;
import java.util.List;

public class Food {

    private int i = 0;
    private int j = 0;

    private int rows;
    private int columns;
    private int[][] boardMatrix;

    public Food(int[][] boardMatrix, int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.boardMatrix = boardMatrix;
        putRandom();
    }

    public void putRandom() {
        boardMatrix[i][j] = CellType.EMPTY_CELL;

        List<Integer> freeCells = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (boardMatrix[i][j] == CellType.EMPTY_CELL) {
                    freeCells.add(i * columns + j);
                }
            }
        }

        int rCellPosition = generateRandom(freeCells.size());

        i = freeCells.get(rCellPosition) / columns;
        j = freeCells.get(rCellPosition) % columns;

        if (boardMatrix[i][j] == CellType.SNAKE_CELL) {
            System.out.println("ups");
        }

        boardMatrix[i][j] = CellType.FOOD_CELL;
    }

    private int generateRandom(int n) {
        return ((int)(Math.random() * 100000)) % n;
    }

    public boolean isEaten(int snakeI, int snakeJ) {
        return snakeI == i && snakeJ == j;
    }

    public void updateFood() {
        boardMatrix[i][j] = CellType.FOOD_CELL;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public void hide() {
        boardMatrix[i][j] = CellType.EMPTY_CELL;
    }
}
