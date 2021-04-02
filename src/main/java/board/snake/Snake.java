package board.snake;

import board.CellType;
import observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    private final List<Observer> snakeCollisionObserverList;

    private final int[] direction = new int[]{0, 1};

    private final List<SnakeCell> snakeCells;

    private final int[][] boardMatrix;
    private final int rows;
    private final int columns;

    private boolean moveConsumed = true;
    private boolean snakeIncreaseConsumed = true;

    public Snake(int[][] boardMatrix, int rows, int columns) {
        this.boardMatrix = boardMatrix;
        this.rows = rows;
        this.columns = columns;
        this.snakeCells = new ArrayList<>();
        snakeCollisionObserverList = new ArrayList<>();
        initialize();
    }

    public void resetSnake() {
        initialize();
    }

    private void initialize() {
        snakeCells.forEach(snakeCell -> {
            int i = snakeCell.getI();
            int j = snakeCell.getJ();
            boardMatrix[i][j] = CellType.EMPTY_CELL;
        });

        snakeCells.clear();

        this.snakeCells.add(new SnakeCell(1, 5));
        this.snakeCells.add(new SnakeCell(1, 4));
        this.snakeCells.add(new SnakeCell(1, 3));
        this.snakeCells.add(new SnakeCell(1, 2));

        direction[0] = 0;
        direction[1] = 1;

        moveConsumed = true;
        snakeIncreaseConsumed = true;
    }

    public void onCrashListener(Observer observer) {
        snakeCollisionObserverList.add(observer);
    }

    public void makeStep() {

        int newI = getNextI();
        int newJ = getNextJ();

        if (newI >= rows || newJ >= columns || newI < 0 || newJ < 0) {
            onSnakeCollide();
            return;
        }

        if (boardMatrix[newI][newJ] == CellType.SNAKE_CELL) {
            onSnakeCollide();
            return;
        }

        SnakeCell head = snakeCells.get(0);
        boardMatrix[head.getI()][head.getJ()] = CellType.SNAKE_CELL;

        SnakeCell endTail = snakeCells.get(snakeCells.size() - 1);
        SnakeCell copyEndTail = endTail.getCopy();

        endTail.updatePosition(newI, newJ);
        snakeCells.remove(endTail);

        snakeCells.add(0, endTail);
        boardMatrix[getHeadI()][getHeadJ()] = CellType.SNAKE_HEAD_CELL;

        if (!snakeIncreaseConsumed) {
            snakeCells.add(copyEndTail);
            snakeIncreaseConsumed = true;
        } else {
            boardMatrix[copyEndTail.getI()][copyEndTail.getJ()] = CellType.EMPTY_CELL;
        }

        moveConsumed = true;
    }

    private void onSnakeCollide() {
        snakeCollisionObserverList.forEach(Observer::update);
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

    public int getNextI() {
        return snakeCells.get(0).getI() + direction[0];
    }

    public int getNextJ() {
        return snakeCells.get(0).getJ() + direction[1];
    }

    public int getHeadI() {
        return snakeCells.get(0).getI();
    }

    public int getHeadJ() {
        return snakeCells.get(0).getJ();
    }

    public void setIncrease() {
        snakeIncreaseConsumed = false;
    }
}
