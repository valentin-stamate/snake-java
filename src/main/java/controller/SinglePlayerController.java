package controller;

import board.snake.Snake;
import processing.core.PApplet;
import processing.event.KeyEvent;

public class SinglePlayerController extends SnakeController {

    private final Snake snake;

    public SinglePlayerController(PApplet pApplet) {
        super(pApplet);
        super.snakeList.add(new Snake(super.board.getBoardMatrix()));
        this.snake = super.snakeList.get(0);
    }

    @Override
    public void run() {
        super.board.start();
        super.pApplet.registerMethod("keyEvent", this);
    }

    public void keyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.PRESS) {
            moveByRightController(event, snake);
        }
    }

}
