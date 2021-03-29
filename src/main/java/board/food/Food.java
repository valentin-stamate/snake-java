package board.food;

import board.CellType;

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

    private void putRandom() {
        int randomI = ((int)(Math.random() * 1000)) % rows;
        int randomJ = ((int)(Math.random() * 1000)) % columns;

        boardMatrix[i][j] = CellType.EMPTY_CELL;

        i = randomI;
        j = randomJ;

        boardMatrix[i][j] = CellType.FOOD_CELL;
    }

    public boolean isEaten(int snakeI, int snakeJ) {
        if (snakeI == i && snakeJ == j) {
            putRandom();
            return true;
        }

        return false;
    }

}
