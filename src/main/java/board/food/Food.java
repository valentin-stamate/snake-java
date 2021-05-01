package board.food;

import board.CellType;

import java.util.ArrayList;
import java.util.List;

public class Food {

    private int i = 0;
    private int j = 0;

    private final int[][] boardMatrix;

    public Food(int[][] boardMatrix) {
        this.boardMatrix = boardMatrix;
    }

    public void updateFoodPosition(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public void drawFood() {
        boardMatrix[i][j] = CellType.FOOD_CELL;
    }
}
