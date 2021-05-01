package controller;

import board.Board;
import board.snake.Snake;
import processing.core.PApplet;
import processing.event.KeyEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class SnakeController {
    protected final Board board;
    protected final List<Snake> snakeList;
    protected final PApplet pApplet;

    SnakeController(PApplet pApplet) {
        this.pApplet = pApplet;
        this.snakeList = new ArrayList<>();
        this.board = new Board(pApplet, snakeList);
    }

    public abstract void run();

    protected void moveByRightController(KeyEvent event, Snake snake) {
        int keyCode = event.getKeyCode();

        final int UP = pApplet.UP;
        final int LEFT = pApplet.LEFT;
        final int RIGHT = pApplet.RIGHT;
        final int BOTTOM = pApplet.DOWN;

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

    protected void moveByLeftController(KeyEvent event, Snake snake) {
        char keyCodeChar = event.getKey();

        if (keyCodeChar == 'w') {
            snake.moveUp();
        } else if (keyCodeChar == 'a') {
            snake.moveLeft();
        } else if (keyCodeChar == 's') {
            snake.moveDown();
        } else if (keyCodeChar == 'd') {
            snake.moveRight();
        }
    }
}
