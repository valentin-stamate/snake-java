package board;

import board.snake.Snake;
import genetic_algorithm.GeneticAlgorithm;
import processing.core.PApplet;
import processing.event.KeyEvent;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final PApplet parentContext;

    private final int xStart;
    private final int yStart;
    private final int rows;
    private final int columns;

    private final int cellSize;

    private final int[][] boardMatrix;

    private final List<Snake> snakeList;

    private GeneticAlgorithm<Snake> geneticAlgorithm;

    private final int gameType;

    public Board(PApplet parent, int xStart, int yStart, int rows, int columns, int cellSize, int gameType) {
        this.parentContext = parent;
        this.xStart = xStart;
        this.yStart = yStart;
        this.rows = rows;
        this.columns = columns;
        this.cellSize = cellSize;
        this.snakeList = new ArrayList<>();
        this.gameType = gameType;

        this.boardMatrix = new int[rows][columns];

        switch (gameType) {
            case GameType.SINGLE_PLAYER:
                addSnake();
                break;
            case GameType.TWO_PLAYERS:
                addSnake();
                addSnake();
                break;
            case GameType.SNAKE_AI:
                for (int i = 0; i < GeneticAlgorithm.POPULATION_SIZE; i++) {
                    addSnake();
                }
                this.geneticAlgorithm = new GeneticAlgorithm<>(snakeList);
                break;
        }

        parentContext.registerMethod("draw", this);
        parentContext.registerMethod("keyEvent", this);
    }

    private void addSnake() {
        snakeList.add(new Snake(boardMatrix, rows, columns, cellSize * columns, cellSize * rows));
    }

    public void draw() {

        clearBoard();

        drawSnakes();

        if (parentContext.frameCount % 1 == 0) {
            makeSnakeStep();

            if (gameType == GameType.SNAKE_AI) {
                checkPopulation();
            }

            predictSnakesMovement();
        }

        drawBoard();


    }

    private void predictSnakesMovement() {
        for (Snake snake : snakeList) {
            snake.makeNextMove();
        }
    }

    private void checkPopulation() {
        boolean allSnakesDead = true;
        for (Snake snake : snakeList) {
            if (!snake.isFinished()) {
                allSnakesDead = false;
                break;
            }
        }

        if (allSnakesDead) {
            geneticAlgorithm.nextGeneration();
            System.out.println("Generation " + geneticAlgorithm.getGeneration());

            reinitializeSnakes();
        }
    }

    private void clearBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                boardMatrix[i][j] = CellType.EMPTY_CELL;
            }
        }
    }

    void drawSnakes() {
        for (Snake snake : snakeList) {
            snake.drawSnake();
        }
    }

    void makeSnakeStep() {
        for (Snake snake : snakeList) {
            snake.makeStep();
        }
    }

    void drawBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                parentContext.strokeWeight(1.5f);
                parentContext.stroke(20);

                switch (boardMatrix[i][j]) {
                    case CellType.SNAKE_HEAD_CELL -> parentContext.fill(175);
                    case CellType.FOOD_CELL -> parentContext.fill(235, 64, 52);
                    case CellType.SNAKE_CELL -> parentContext.fill(255);
                    default -> {
                        parentContext.fill(20);
                        parentContext.stroke(255);
                        parentContext.strokeWeight(0);
                    }
                }
                parentContext.rect(xStart + j * cellSize, yStart + i * cellSize, cellSize, cellSize);
            }
        }

        int boardPWidth = columns * cellSize;
        int boardPHeight = rows * cellSize;

        parentContext.stroke(255);
        parentContext.strokeWeight(2);
        parentContext.line(xStart, yStart, xStart, yStart + boardPHeight);
        parentContext.line(xStart, yStart, xStart + boardPWidth, yStart);
        parentContext.line(xStart + boardPWidth, yStart, xStart + boardPWidth, yStart + boardPHeight);
        parentContext.line(xStart, yStart + boardPHeight, xStart + boardPWidth, yStart + boardPHeight);
    }

    private void reinitializeSnakes() {
        for (Snake snake : snakeList) {
            snake.initialize();
        }
    }

    public void keyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.PRESS) {
            if (gameType == GameType.SINGLE_PLAYER) {
                movePlayerOne(event);
            } else if (gameType == GameType.TWO_PLAYERS) {
                movePlayerOne(event);
                movePlayerTwo(event);
            }
        }
    }

    public void movePlayerOne(KeyEvent event) {
        int keyCode = event.getKeyCode();

        final int UP = parentContext.UP;
        final int LEFT = parentContext.LEFT;
        final int RIGHT = parentContext.RIGHT;
        final int BOTTOM = parentContext.DOWN;

        Snake snakeOne = snakeList.get(0);

        if (keyCode == UP) {
            snakeOne.moveUp();
        } else if (keyCode == LEFT) {
            snakeOne.moveLeft();
        } else if (keyCode == RIGHT) {
            snakeOne.moveRight();
        } else if (keyCode == BOTTOM) {
            snakeOne.moveDown();
        }

    }

    private void movePlayerTwo(KeyEvent event) {
        char keyCodeChar = event.getKey();

        Snake snakeTwo = snakeList.get(1);

        if (keyCodeChar == 'w') {
            snakeTwo.moveUp();
        } else if (keyCodeChar == 'a') {
            snakeTwo.moveLeft();
        } else if (keyCodeChar == 's') {
            snakeTwo.moveDown();
        } else if (keyCodeChar == 'd') {
            snakeTwo.moveRight();
        }
    }

}
