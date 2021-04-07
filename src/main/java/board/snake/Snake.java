package board.snake;

import board.CellType;
import observer.Observer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Snake {

    private final List<Observer> snakeCollisionObserverList;

    private final int[] direction = new int[]{0, 1};

    private final List<SnakeCell> snakeCells;
    private final HashSet<SnakeCell> snakeCellsHashSet;

    private final int[][] boardMatrix;
    private final int rows;
    private final int columns;

    private boolean moveConsumed = true;
    private boolean snakeIncreaseConsumed = true;
    private boolean snakeFinished = false;

    public Snake(int[][] boardMatrix, int rows, int columns) {
        this.boardMatrix = boardMatrix;
        this.rows = rows;
        this.columns = columns;
        this.snakeCells = new ArrayList<>();
        this.snakeCellsHashSet = new HashSet<>();
        snakeCollisionObserverList = new ArrayList<>();
        initialize();
    }

    private void initialize() {
        deleteSnake();

        int i = generateRandom(5, rows - 5);
        int j = generateRandom(5, columns - 5);

        int startingLength = 4;

        for (int l = 0; l < startingLength; l++) {
            SnakeCell lastSnakeCell = new SnakeCell(i, j);
            boardMatrix[lastSnakeCell.getI()][lastSnakeCell.getJ()] = CellType.SNAKE_CELL;

            List<SnakeCell> neighbours = getSnakeCellNeighbours(lastSnakeCell);
            snakeCells.add(lastSnakeCell);
            snakeCellsHashSet.add(lastSnakeCell);

            SnakeCell randomCell = neighbours.get(generateRandom(0, neighbours.size()));

            i = randomCell.getI();
            j = randomCell.getJ();

            if (l == 0) {
                direction[0] = lastSnakeCell.getI() - randomCell.getI();
                direction[1] = lastSnakeCell.getJ() - randomCell.getJ();
            }
        }

        moveConsumed = true;
        snakeIncreaseConsumed = true;
    }

    private List<SnakeCell> getSnakeCellNeighbours(SnakeCell snakeCell) {
        /* TODO handle snake intersection */
        List<SnakeCell> neighbours = new ArrayList<>();

        int i = snakeCell.getI();
        int j = snakeCell.getJ();

        if (i > 0) {
            if (boardMatrix[i - 1][j] == CellType.EMPTY_CELL) {
                neighbours.add(new SnakeCell(i - 1, j));
            }
        }

        if (j > 0) {
            if (boardMatrix[i][j - 1] == CellType.EMPTY_CELL) {
                neighbours.add(new SnakeCell(i, j - 1));
            }
        }

        if (i + 1 < rows) {
            if (boardMatrix[i + 1][j] == CellType.EMPTY_CELL) {
                neighbours.add(new SnakeCell(i + 1, j));
            }
        }

        if (j + 1 < columns) {
            if (boardMatrix[i][j + 1] == CellType.EMPTY_CELL) {
                neighbours.add(new SnakeCell(i, j + 1));
            }
        }

        return neighbours;
    }

    public void deleteSnake() {
        /* TODO handle snake intersection */
        snakeCells.forEach(snakeCell -> {
            int i = snakeCell.getI();
            int j = snakeCell.getJ();
            boardMatrix[i][j] = CellType.EMPTY_CELL;
        });

        snakeCells.clear();
    }

    public void setSnakeFinished() {
        snakeFinished = true;
    }

    public void onCrashListener(Observer observer) {
        snakeCollisionObserverList.add(observer);
    }

    public void makeStep() {
        if (snakeFinished) {
            return;
        }

        int newI = getNextI();
        int newJ = getNextJ();

        if (newI >= rows || newJ >= columns || newI < 0 || newJ < 0) {
            onSnakeCollide();
            return;
        }

        if (snakeCellsHashSet.contains(new SnakeCell(newI, newJ))) {
            onSnakeCollide();
            return;
        }

        SnakeCell head = snakeCells.get(0);
        boardMatrix[head.getI()][head.getJ()] = CellType.SNAKE_CELL;

        SnakeCell endTail = snakeCells.get(snakeCells.size() - 1);
        SnakeCell copyEndTail = endTail.getCopy();

        snakeCells.remove(endTail);
        snakeCellsHashSet.remove(endTail);

        endTail = new SnakeCell(newI, newJ);

        snakeCells.add(0, endTail);
        snakeCellsHashSet.add(endTail);

        boardMatrix[getHeadI()][getHeadJ()] = CellType.SNAKE_HEAD_CELL;

        if (!snakeIncreaseConsumed) {
            snakeCells.add(copyEndTail);
            snakeCellsHashSet.add(copyEndTail);
            snakeIncreaseConsumed = true;
        } else {
            boardMatrix[copyEndTail.getI()][copyEndTail.getJ()] = CellType.EMPTY_CELL;
        }

        moveConsumed = true;
        updateSnakeTail();
    }

    private void updateSnakeTail() {
        for (int i = 0; i < snakeCells.size(); i++) {
            SnakeCell snakeCell = snakeCells.get(i);
            boardMatrix[snakeCell.getI()][snakeCell.getJ()] = i == 0 ? CellType.SNAKE_HEAD_CELL : CellType.SNAKE_CELL;
        }
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

    private int generateRandom(int start, int end) {
           return (((int)(Math.random() * 10000)) % (end - start)) + start;
    }

    public boolean isFinished() {
        return snakeFinished;
    }
}
