package board;

import board.snake.Snake;
import observer.Observer;
import processing.core.PApplet;
import util.Config;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final int[][] boardMatrix;
    private final List<Snake> snakeList;
    private final PApplet pApplet;

    private final List<Observer> observers;

    public Board(PApplet pApplet, List<Snake> snakeList) {
        this.pApplet = pApplet;
        this.snakeList = snakeList;
        this.observers = new ArrayList<>();

        this.boardMatrix = new int[Config.BOARD_ROWS][Config.BOARD_COLUMNS];

    }

    public void start() {
        this.pApplet.registerMethod("draw", this);
    }

    /* OBSERVER */
    public void setOnRefreshListener(Observer observer) {
        observers.add(observer);
    }

    /* SNAKES CONTROL */
    void makeSnakeStep() {
        for (Snake snake : snakeList) {
            snake.makeStep();
        }
    }

    /* DRAWING LOGIC */
    public void draw() {
        clearBoard();
        drawSnakes();

        if (pApplet.frameCount % Config.REFRESH_RATE == 0) {
            makeSnakeStep();

            for (Observer observer : observers) {
                observer.update();
            }
        }

        drawBoard();
    }

    void drawSnakes() {
        for (Snake snake : snakeList) {
            snake.drawSnake();
        }
    }

    void drawBoard() {
        for (int i = 0; i < Config.BOARD_ROWS; i++) {
            for (int j = 0; j < Config.BOARD_COLUMNS; j++) {
                pApplet.strokeWeight(1.5f);
                pApplet.stroke(20);

                switch (boardMatrix[i][j]) {
                    case CellType.SNAKE_HEAD_CELL:
                        pApplet.fill(175);
                        break;
                    case CellType.FOOD_CELL:
                        pApplet.fill(235, 64, 52);
                        break;
                    case CellType.SNAKE_CELL:
                        pApplet.fill(255);
                        break;
                    default: {
                        pApplet.fill(20);
                        pApplet.stroke(255);
                        pApplet.strokeWeight(0);
                    }
                }
                pApplet.rect(Config.BOARD_X + j * Config.CELL_SIZE, Config.BOARD_Y + i * Config.CELL_SIZE, Config.CELL_SIZE, Config.CELL_SIZE);
            }
        }

        int boardPWidth = Config.BOARD_COLUMNS * Config.CELL_SIZE;
        int boardPHeight = Config.BOARD_ROWS * Config.CELL_SIZE;

        pApplet.stroke(255);
        pApplet.strokeWeight(2);
        pApplet.line(Config.BOARD_X, Config.BOARD_Y, Config.BOARD_X, Config.BOARD_Y + boardPHeight);
        pApplet.line(Config.BOARD_X, Config.BOARD_Y, Config.BOARD_X + boardPWidth, Config.BOARD_Y);
        pApplet.line(Config.BOARD_X + boardPWidth, Config.BOARD_Y, Config.BOARD_X + boardPWidth, Config.BOARD_Y + boardPHeight);
        pApplet.line(Config.BOARD_X, Config.BOARD_Y + boardPHeight, Config.BOARD_X + boardPWidth, Config.BOARD_Y + boardPHeight);
    }

    private void clearBoard() {
        for (int i = 0; i < Config.BOARD_ROWS; i++) {
            for (int j = 0; j < Config.BOARD_COLUMNS; j++) {
                boardMatrix[i][j] = CellType.EMPTY_CELL;
            }
        }
    }

    /* GETTERS AND SETTERS */
    public int[][] getBoardMatrix() {
        return boardMatrix;
    }

}
