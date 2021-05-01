package board;

import board.snake.Snake;
import genetic_algorithm.GeneticAlgorithm;
import processing.core.PApplet;
import processing.event.KeyEvent;
import util.Config;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final int[][] boardMatrix;
    private final List<Snake> snakeList;
    private final PApplet pApplet;

//    private GeneticAlgorithm<Snake> geneticAlgorithm;

    public Board(PApplet pApplet, List<Snake> snakeList) {
        this.pApplet = pApplet;
        this.snakeList = snakeList;
//        this.parentContext = parent;
//        this.snakeList = new ArrayList<>();

        this.boardMatrix = new int[Config.BOARD_ROWS][Config.BOARD_COLUMNS];

//        switch (gameType) {
//            case GameType.SINGLE_PLAYER:
//                addSnake();
//                break;
//            case GameType.TWO_PLAYERS:
//                addSnake();
//                addSnake();
//                break;
//            case GameType.SNAKE_AI:
//                for (int i = 0; i < GeneticAlgorithm.POPULATION_SIZE; i++) {
//                    addSnake();
//                }
//                this.geneticAlgorithm = new GeneticAlgorithm<>(snakeList);
//                break;
//        }

    }

//    private void addSnake() {
//        snakeList.add(new Snake(boardMatrix, rows, columns, cellSize * columns, cellSize * rows));
//    }

    public int[][] getBoardMatrix() {
        return boardMatrix;
    }

    public void start() {
        this.pApplet.registerMethod("draw", this);
    }

    public void draw() {

        clearBoard();

        drawSnakes();

        if (pApplet.frameCount % Config.REFRESH_RATE == 0) {
            makeSnakeStep();

//            if (gameType == GameType.SNAKE_AI) {
//                checkPopulation();
//            }

//            predictSnakesMovement();
            /* TODO, add an observer */
        }

        drawBoard();


    }

//    private void predictSnakesMovement() {
//        for (Snake snake : snakeList) {
//            snake.makeNextMove();
//        }
//    }

//    private void checkPopulation() {
//        boolean allSnakesDead = true;
//        for (Snake snake : snakeList) {
//            if (!snake.isFinished()) {
//                allSnakesDead = false;
//                break;
//            }
//        }
//
//        if (allSnakesDead) {
//            geneticAlgorithm.nextGeneration();
//            System.out.println("Generation " + geneticAlgorithm.getGeneration());
//
//            reinitializeSnakes();
//        }
//    }

    private void clearBoard() {
        for (int i = 0; i < Config.BOARD_ROWS; i++) {
            for (int j = 0; j < Config.BOARD_COLUMNS; j++) {
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
//
//    private void reinitializeSnakes() {
//        for (Snake snake : snakeList) {
//            snake.initialize();
//        }
//    }
//


}
