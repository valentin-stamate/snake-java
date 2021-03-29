package board;

import board.snake.Snake;
import processing.core.PApplet;
import processing.event.KeyEvent;

public class Board {

    private final PApplet parentContext;

    private final int xStart;
    private final int yStart;
    private final int rows;
    private final int columns;

    private final int cellSize;

    private final int[][] boardMatrix;

    private final Snake snake;

    public Board(PApplet parent, int xStart, int yStart, int rows, int columns, int cellSize) {
        this.parentContext = parent;
        this.xStart = xStart;
        this.yStart = yStart;
        this.rows = rows;
        this.columns = columns;
        this.cellSize = cellSize;

        this.boardMatrix = new int[rows][columns];

        snake = new Snake(boardMatrix);

        parentContext.registerMethod("draw", this);
        parentContext.registerMethod("keyEvent", this);
    }

    public void draw() {

        if (parentContext.frameCount % 40 == 0) {
            snake.makeStep();
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                switch (boardMatrix[i][j]) {
                    case CellType.SNAKE_HEAD_CELL:
                        parentContext.fill(235, 64, 52);
                        break;
                    default:
                        parentContext.fill(20);
                        break;
                }
                parentContext.stroke(255);
                parentContext.rect(xStart + j * cellSize, yStart + i * cellSize, cellSize, cellSize);
            }
        }
    }

    public void keyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.PRESS) {
            keyPressed(event.getKeyCode());
        }
    }

    public void keyPressed(int keyCode) {
        final int UP = parentContext.UP;
        final int LEFT = parentContext.LEFT;
        final int RIGHT = parentContext.RIGHT;
        final int BOTTOM = parentContext.DOWN;

        if (!snake.haveMoveConsumed()) {
            return;
        }

        if (keyCode == UP) {
            snake.moveUp();
        } else if (keyCode == LEFT) {
            snake.moveLeft();
        } else if (keyCode == RIGHT) {
            snake.moveRight();
        } else if (keyCode == BOTTOM) {
            snake.moveDown();
        }
    }

}
