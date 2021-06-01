package controller;

import board.snake.Snake;
import board.snake.SnakeData;
import observer.OnFoodEaten;
import panel.SideElements;
import processing.core.PApplet;
import processing.event.KeyEvent;
import request.Backend;

public class SinglePlayerController extends SnakeController {

    private final Snake snake;

    public SinglePlayerController(PApplet pApplet) {
        super(pApplet);
        this.snake = new Snake(super.board.getBoardMatrix());
        super.snakeList.add(snake);

        SideElements.updateScoreList();

        snake.addOnFinishObserver((data) -> {
            SnakeData snakeData = (SnakeData) data;
            Backend backend = new Backend();

            new Thread(() -> {
                backend.send(snakeData);
                SideElements.updateScoreList();
            }).start();

            SideElements.updateScoreList();
        });

        snake.addObserver((OnFoodEaten) () -> {
            SideElements.setPlayerOneScore(snake.getScore());
        });
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
