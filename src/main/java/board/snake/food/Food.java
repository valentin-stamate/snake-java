package board.snake.food;

import board.CellType;

public class Food {

    private int i = 0;
    private int j = 0;

    private final int[][] boardMatrix;

    public Food(int[][] boardMatrix) {
        this.boardMatrix = boardMatrix;
    }

    /* GETTERS AND SETTERS */
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

    /* DRAWING LOGIC */
    public void drawFood() {
        boardMatrix[i][j] = CellType.FOOD_CELL;
    }

}
