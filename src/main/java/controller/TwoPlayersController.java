package controller;

import board.snake.Snake;
import board.snake.SnakeData;
import observer.OnFoodEaten;
import panel.SideElements;
import processing.core.PApplet;
import processing.event.KeyEvent;
import request.Backend;

public class TwoPlayersController extends SnakeController{

    private final Snake snakeA;
    private final Snake snakeB;

    public TwoPlayersController(PApplet pApplet) {
        super(pApplet);
        this.snakeA = new Snake(super.board.getBoardMatrix());
        this.snakeB = new Snake(super.board.getBoardMatrix());
        super.snakeList.add(snakeA);
        super.snakeList.add(snakeB);

        SideElements.updateScoreList();

        snakeA.addOnFinishObserver((data) -> {
            SnakeData snakeData = (SnakeData) data;
            Backend backend = new Backend();

            new Thread(() -> {
                backend.send(snakeData);
                SideElements.updateScoreList();
            }).start();

            SideElements.updateScoreList();
        });

        snakeA.addObserver((OnFoodEaten) () -> {
            SideElements.setPlayerOneScore(snakeA.getScore());
        });

        snakeB.addOnFinishObserver((data) -> {
            SnakeData snakeData = (SnakeData) data;
            Backend backend = new Backend();

            new Thread(() -> {
                backend.send(snakeData);
                SideElements.updateScoreList();
            }).start();

            SideElements.updateScoreList();
        });

        snakeB.addObserver((OnFoodEaten) () -> {
            SideElements.setPlayerTwoScore(snakeB.getScore());
        });
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
