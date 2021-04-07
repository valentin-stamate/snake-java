package board;

import board.food.Food;
import board.snake.Snake;
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
    private final Food food;

    public Board(PApplet parent, int xStart, int yStart, int rows, int columns, int cellSize) {
        this.parentContext = parent;
        this.xStart = xStart;
        this.yStart = yStart;
        this.rows = rows;
        this.columns = columns;
        this.cellSize = cellSize;
        this.snakeList = new ArrayList<>();

        this.boardMatrix = new int[rows][columns];

        snakeList.add(new Snake(boardMatrix, rows, columns));
        snakeList.add(new Snake(boardMatrix, rows, columns));

        food = new Food(boardMatrix, rows, columns);

        parentContext.registerMethod("draw", this);
        parentContext.registerMethod("keyEvent", this);

        snakeList.forEach(snake -> {
            snake.onCrashListener(() -> {
                snake.deleteSnake();
                snake.setSnakeFinished();
            });
        });
    }

    public void draw() {

        if (parentContext.frameCount % 3 == 0) {
            for (Snake snake : snakeList) {
                if (snake.isFinished()) {
                    continue;
                }

                int snakeHeadI = snake.getNextI();
                int snakeHeadJ = snake.getNextJ();

                if (food.isEaten(snakeHeadI, snakeHeadJ)) {
                    snake.setIncrease();
                }

                snake.makeStep();
            }

        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                switch (boardMatrix[i][j]) {
                    case CellType.SNAKE_HEAD_CELL:
                        parentContext.fill(175);
                        break;
                    case CellType.FOOD_CELL:
                        parentContext.fill(235, 64, 52);
                        break;
                    case CellType.SNAKE_CELL:
                        parentContext.fill(255);
                        break;
                    default:
                        parentContext.fill(20);
                        break;
                }
                parentContext.strokeWeight(0);
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

    public void keyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.PRESS) {
            movePlayerOne(event);
            movePlayerTwo(event);
        }
    }

    public void movePlayerOne(KeyEvent event) {
        int keyCode = event.getKeyCode();

        final int UP = parentContext.UP;
        final int LEFT = parentContext.LEFT;
        final int RIGHT = parentContext.RIGHT;
        final int BOTTOM = parentContext.DOWN;

        Snake snakeOne = snakeList.get(0);

        if (!snakeOne.haveMoveConsumed()) {
            return;
        }

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

        if (snakeTwo == null) {
            return;
        }

        if (!snakeTwo.haveMoveConsumed()) {
            return;
        }

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
