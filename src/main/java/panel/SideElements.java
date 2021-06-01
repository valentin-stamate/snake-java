package panel;

import board.snake.SnakeData;
import processing.core.PApplet;
import request.Backend;
import util.Config;
import java.util.ArrayList;
import java.util.List;

public class SideElements {

    private static List<SnakeData> scoreList = new ArrayList<>();
    private static int playerOneScore = 0;
    private static int playerTwoScore = 0;

    private static int LEFT_OFFSET = Config.BOARD_WIDTH + 10;

    public static void drawTitle(PApplet pApplet) {
        pApplet.textSize(18);
        pApplet.fill(255);
        pApplet.textAlign(pApplet.LEFT, pApplet.TOP);
        pApplet.text("Conquering The Snake", LEFT_OFFSET + 8, 20);
    }

    public static void drawScore(PApplet pApplet) {
        final int offset = 400;

        pApplet.fill(255);
        pApplet.textAlign(pApplet.LEFT, pApplet.TOP);

        pApplet.textSize(16);
        pApplet.text(" --== Top Players ==-- ", LEFT_OFFSET + 15, offset);

        pApplet.textSize(14);
        for (int i = 0; i < scoreList.size(); i++) {
            SnakeData snakeData = scoreList.get(i);

            String row = String.format("%-10s -> %-10s | %-10s", snakeData.name, "" + snakeData.score, snakeData.boardSize);
            pApplet.text(row, LEFT_OFFSET + 3, offset + (i + 1) * 20 + 5);
        }

    }

    public static void drawScoreFirstPlayer(PApplet pApplet) {
        pApplet.fill(255);
        pApplet.textAlign(pApplet.LEFT, pApplet.TOP);

        String row = String.format("Player One: %d", playerOneScore);

        pApplet.textSize(14);
        pApplet.text(row, LEFT_OFFSET, 60);
    }

    public static void drawScoreSecondPlayer(PApplet pApplet) {
        pApplet.fill(255);
        pApplet.textAlign(pApplet.LEFT, pApplet.TOP);

        String row = String.format("Player Two: %d", playerTwoScore);

        pApplet.textSize(14);
        pApplet.text(row, LEFT_OFFSET + 130, 60);
    }

    /* GETTERS AND SETTERS */
    public static void updateScoreList() {
        new Thread(() -> scoreList = List.of(new Backend().receive())).start();
    }

    public static void setPlayerOneScore(int score) {
        playerOneScore = score;
    }

    public static void setPlayerTwoScore(int score) {
        playerTwoScore = score;
    }

}
