package board.snake;

import board.CellType;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    private final int[] direction = new int[]{0, 1};

    private final SnakeCell shakeHead;
    private final List<SnakeCell> tail;

    private final int[][] boardMatrix;

    private boolean moveConsumed = true;

    public Snake(int[][] boardMatrix) {
        this.shakeHead = new SnakeCell(1, 1);
        this.boardMatrix = boardMatrix;
        tail = new ArrayList<>();
    }

    public void makeStep() {
        int i = shakeHead.getI();
        int j = shakeHead.getJ();
        boardMatrix[i][j] = CellType.EMPTY_CELL;

        int newI = getNextI();
        int newJ = getNextJ();

        /* TODO Verify collision with tail and wall */
        shakeHead.updatePosition(newI, newJ);

        boardMatrix[newI][newJ] = CellType.SNAKE_HEAD_CELL;
        moveConsumed = true;
    }

    public boolean haveMoveConsumed() {
        return moveConsumed;
    }

    public void moveUp() {
        if (direction[0] == 1) {
            return;
        }

        direction[0] = -1;
        direction[1] = 0;
        moveConsumed = false;
    }

    public void moveRight() {
        if (direction[1] == -1) {
            return;
        }

        direction[0] = 0;
        direction[1] = 1;
        moveConsumed = false;
    }

    public void moveLeft() {
        if (direction[1] == 1) {
            return;
        }

        direction[0] = 0;
        direction[1] = -1;
        moveConsumed = false;
    }

    public void moveDown() {
        if (direction[0] == -1) {
            return;
        }

        direction[0] = 1;
        direction[1] = 0;
        moveConsumed = false;
    }

    private int getNextI() {
        return shakeHead.getI() + direction[0];
    }

    private int getNextJ() {
        return shakeHead.getJ() + direction[1];
    }

}
