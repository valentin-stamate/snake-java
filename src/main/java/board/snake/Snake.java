package board.snake;

import board.CellType;
import board.snake.food.Food;
import controller.controlers.genetic.GeneticAiUtil;
import observer.Observer;
import observer.OnFinishObserver;
import observer.OnFoodEaten;
import util.Config;
import util.Util;
import java.util.*;

public class Snake {

    /* dir[0] -> i, dir[1] -> j */
    private final int[] direction = new int[]{0, 1};
    private final int[][] boardMatrix;

    private final List<SnakeCell> snakeCells;
    private final HashSet<SnakeCell> snakeCellsHashSet;

    private boolean canChangeDirection = true;
    private boolean snakeIncrease = true;
    private boolean snakeFinished = false;

    private final Food food;
    private int score;

    private final List<OnFinishObserver> onFinishObservers;
    private final List<Observer> observerList;

    public Snake(int[][] boardMatrix) {
        this.boardMatrix = boardMatrix;
        this.snakeCells = new ArrayList<>();
        this.snakeCellsHashSet = new HashSet<>();
        this.onFinishObservers = new ArrayList<>();
        this.observerList = new ArrayList<>();
        this.score = 0;

        this.food = new Food(boardMatrix);
        initialize();
    }

    /* INITIALIZERS */
    public void initialize() {
        snakeCells.clear();
        snakeCellsHashSet.clear();

        direction[0] = Util.generateRandom(-1, 2);
        direction[1] = 0;
        if (direction[0] == 0) {
            direction[1] = Util.generateRandom(-1, 2);
        }

        initializeSnakePosition();

        canChangeDirection = true;
        snakeIncrease = false;
        snakeFinished = false;
        score = 0;
        repositionFood();
    }

    private void initializeSnakePosition() {
        int currentI = Config.BOARD_ROWS / 2 + Util.generateRandom(-7, 7);
        int currentJ = Config.BOARD_COLUMNS / 2 + Util.generateRandom(-7, 7);

        if (Config.BOARD_COLUMNS < 10) {
            currentI = 2;
            currentJ = 2;
        }

        int startingLength = 4;

        for (int i = 0; i < startingLength; i++) {
            snakeCells.add(new SnakeCell(currentI, currentJ));
            currentI -= direction[0];
            currentJ -= direction[1];

            if (currentI < 0 || currentI >= Config.BOARD_ROWS) {
                break;
            }

            if (currentJ < 0 || currentJ >= Config.BOARD_COLUMNS) {
                break;
            }
        }
    }

    /* MOVING THE SNAKE */
    public void makeStep() {
        if (snakeFinished) {
            return;
        }

        int newI = getNextI();
        int newJ = getNextJ();

        if (newI >= Config.BOARD_ROWS || newJ >= Config.BOARD_COLUMNS || newI < 0 || newJ < 0) {
            onSnakeCollide();
            return;
        }

        if (checkTailCollision(newI, newJ)) {
            onSnakeCollide();
            return;
        }

        if (checkFoodCollision(newI, newJ)) {
            addPoint();
            repositionFood();
        }

        SnakeCell endTail = snakeCells.get(snakeCells.size() - 1);
        SnakeCell copyEndTail = endTail.getCopy();

        snakeCells.remove(endTail);
        snakeCellsHashSet.remove(endTail);

        SnakeCell newHead = new SnakeCell(newI, newJ);

        snakeCells.add(0, newHead);
        snakeCellsHashSet.add(newHead);

        if (snakeIncrease) {
            snakeCells.add(copyEndTail);
            snakeCellsHashSet.add(copyEndTail);
            snakeIncrease = false;
        }

        canChangeDirection = true;
    }

    /* DRAWING LOGIC */
    public void drawSnake() {
        if (snakeFinished) {
            return;
        }

        for (int i = 0; i < snakeCells.size(); i++) {
            SnakeCell snakeCell = snakeCells.get(i);
            boardMatrix[snakeCell.getI()][snakeCell.getJ()] = i == 0 ? CellType.SNAKE_HEAD_CELL : CellType.SNAKE_CELL;
        }
        food.drawFood();
    }

    /* SNAKE INTERACTION */
    private boolean checkTailCollision(int headI, int headJ) {
        return snakeCellsHashSet.contains(new SnakeCell(headI, headJ));
    }

    private void onSnakeCollide() {
        /* TODO, maybe change this logic a little bit? */
        SnakeData snakeData = new SnakeData("Snake", score,
                String.format("%s x %s", Config.BOARD_ROWS, Config.BOARD_COLUMNS));

        for (OnFinishObserver observer : onFinishObservers) {
            observer.update(snakeData);
        }

        snakeFinished = true;
    }

    public boolean checkFoodCollision(int i, int j) {
        return food.getI() == i && food.getJ() == j;
    }

    /* FOOD LOGIC */
    public void repositionFood() {
        List<Integer> freeCells = new ArrayList<>();

        for (int i = 0; i < Config.BOARD_ROWS; i++) {
            for (int j = 0; j < Config.BOARD_COLUMNS; j++) {
                if (!checkTailCollision(i, j)) {
                    freeCells.add(i * Config.BOARD_COLUMNS + j);
                }
            }
        }

        int rCellPosition = Util.generateRandom(0, freeCells.size());

        int i = freeCells.get(rCellPosition) / Config.BOARD_COLUMNS;
        int j = freeCells.get(rCellPosition) % Config.BOARD_COLUMNS;

        food.updateFoodPosition(i, j);
    }

    public void addPoint() {
        snakeIncrease = true;
        score+= 5;

        for (Observer observer : observerList) {
            if (observer instanceof OnFoodEaten) {
                observer.update();
            }
        }
    }

    /* GETTERS AND SETTERS */
    public void addOnFinishObserver(OnFinishObserver observer) {
        onFinishObservers.add(observer);
    }

    public void addObserver(Observer observer) {
        observerList.add(observer);
    }

    private int getNextI() {
        return snakeCells.get(0).getI() + direction[0];
    }

    private int getNextJ() {
        return snakeCells.get(0).getJ() + direction[1];
    }

    public int getHeadI() {
        return snakeCells.get(0).getI();
    }

    public int getHeadJ() {
        return snakeCells.get(0).getJ();
    }

    public int getScore() {
        return score;
    }

    public boolean isFinished() {
        return snakeFinished;
    }

    public int[] getDirection() {
        return new int[]{direction[0], direction[1]};
    }

    /* Gets a 24 double vector  */
    public int[] getVision() {

        // degrees are in reverse because the coords are in reverse
        int[] head = direction;
        int[] west = GeneticAiUtil.reverse(GeneticAiUtil.rotateVectorInt(head[1], head[0], -90));
        int[] south = GeneticAiUtil.reverse(GeneticAiUtil.rotateVectorInt(head[1], head[0], -180));
        int[] east = GeneticAiUtil.reverse(GeneticAiUtil.rotateVectorInt(head[1], head[0], -270));

        double[] rotationDiag =  GeneticAiUtil.reverse(GeneticAiUtil.rotateVector(head[1], head[0], -45));

        int[] northWest = GeneticAiUtil.crushedDiagonalsToDirection(rotationDiag);
        int[] southWest = GeneticAiUtil.reverse(GeneticAiUtil.rotateVectorInt(northWest[1], northWest[0], -90));
        int[] southEast = GeneticAiUtil.reverse(GeneticAiUtil.rotateVectorInt(northWest[1], northWest[0], -180));
        int[] northEast = GeneticAiUtil.reverse(GeneticAiUtil.rotateVectorInt(northWest[1], northWest[0], -270));

        int i = getHeadI();
        int j = getHeadJ();

        int[] headData = getDirectionData(i, j, head);
        int[] westData = getDirectionData(i, j, west);
        int[] southData = getDirectionData(i, j, south);
        int[] eastData = getDirectionData(i, j, east);

        int[] northWestData = getDirectionData(i, j, northWest);
        int[] southWestData = getDirectionData(i, j, southWest);
        int[] southEastData = getDirectionData(i, j, southEast);
        int[] northEastData = getDirectionData(i, j, northEast);

        int[] vision = new int[8 * 3];

        System.arraycopy(headData,      0, vision, 0, 3);
        System.arraycopy(northWestData, 0, vision, 3, 3);
        System.arraycopy(westData,      0, vision, 6, 3);
        System.arraycopy(southWestData, 0, vision, 9, 3);
        System.arraycopy(southData,     0, vision, 12, 3);
        System.arraycopy(southEastData, 0, vision, 15, 3);
        System.arraycopy(eastData,      0, vision, 18, 3);
        System.arraycopy(northEastData, 0, vision, 21, 3);

        return vision;
    }

    public double[] getNormalizedVision() {
        return normalizeDistances(getVision());
    }

    private int[] getDirectionData(int i, int j, int[] direction) {
        int rows = boardMatrix.length;
        int cols = boardMatrix[0].length;

        boolean foodFound = false;
        boolean tailFound = false;

        int foodDistance = 0;
        int tailDistance = 0;
        int wallDistance = 0;

        for (; i < rows && i >= 0 && j < cols && j >= 0;
               i += direction[0], j += direction[1]) {

            if (!foodFound) {
                foodDistance++;
            }

            wallDistance++;

            if (!tailFound) {
                tailDistance++;
            }

            if (checkFoodCollision(i, j)) {
                foodFound = true;
            }

            if (!tailFound && checkTailCollision(i + direction[0], j + direction[1])) {
                tailFound = true;
            }

        }

        int maxDistance = Math.max(rows, cols);

        if (!foodFound) {
            foodDistance = maxDistance;
        }

        if (!tailFound) {
            tailDistance = maxDistance;
        }

        if (tailFound) {
            wallDistance = maxDistance;
        }

        return new int[]{foodDistance, tailDistance, wallDistance};
    }

    private double[] normalizeDistances(int[] distances) {
        int n = distances.length;

        double[] normalizedDistance = new double[n];

        double max = Math.max(boardMatrix.length, boardMatrix[0].length);

        for (int i = 0; i < n; i++) {
            normalizedDistance[i] = 1.0 * distances[i] / max;
        }

        return normalizedDistance;
    }

    /* CHANGING DIRECTION */
    public void moveUp() {
        if (direction[0] == 1 || !canChangeDirection) {
            return;
        }

        direction[0] = -1;
        direction[1] = 0;
        canChangeDirection = false;
    }

    public void moveRight() {
        if (direction[1] == -1 || !canChangeDirection) {
            return;
        }

        direction[0] = 0;
        direction[1] = 1;
        canChangeDirection = false;
    }

    public void moveLeft() {
        if (direction[1] == 1 || !canChangeDirection) {
            return;
        }

        direction[0] = 0;
        direction[1] = -1;
        canChangeDirection = false;
    }

    public void moveDown() {
        if (direction[0] == -1 || !canChangeDirection) {
            return;
        }

        direction[0] = 1;
        direction[1] = 0;
        canChangeDirection = false;
    }
}
