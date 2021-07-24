package controller;

import board.snake.Snake;
import processing.core.PApplet;
import processing.event.KeyEvent;

public class TwoPlayersController extends SnakeController{

    private final Snake snakeA;
    private final Snake snakeB;

    public TwoPlayersController(PApplet pApplet) {
        super(pApplet);
        this.snakeA = new Snake(super.board.getBoardMatrix());
        this.snakeB = new Snake(super.board.getBoardMatrix());
        super.snakeList.add(snakeA);
        super.snakeList.add(snakeB);
    }

    @Override
    public void run() {
        super.board.start();
        super.pApplet.registerMethod("keyEvent", this);
    }

    public void keyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.PRESS) {
            moveByRightController(event, snakeA);
            moveByLeftController(event, snakeB);
        }
    }
}
