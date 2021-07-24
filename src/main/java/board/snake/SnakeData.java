package board.snake;

import java.io.Serializable;

public class SnakeData implements Serializable {
    /* I DIDN'T PUT GETTERS AND SETTERS BECAUSE THE MEMBERS ARE FINAL AND CANNOT CHANGE */
    public final int id;
    public final String name;
    public final int score;
    public final String boardSize;

    public SnakeData(int id, String name, int score, String boardSize) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.boardSize = boardSize;
    }

    public SnakeData(String name, int score, String boardSize) {
        this.name = name;
        this.score = score;
        this.boardSize = boardSize;
        this.id = 0;
    }

    public SnakeData() {
        this.id = 0;
        this.name = "";
        this.score = 0;
        this.boardSize = "";
    }

}
