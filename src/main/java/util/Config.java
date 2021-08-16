package util;

public abstract class Config {
    public static final int GAME_TYPE = GameType.GENETIC_AI;

    /* CANVAS */
    public static final int FRAMERATE = 60;
    public static final int REFRESH_RATE = 1;

    /* BOARD */
    public static final int BOARD_WIDTH = 650;
    public static final int BOARD_HEIGHT = 650;

    public static final int CANVAS_WIDTH = BOARD_WIDTH;
    public static final int CANVAS_HEIGHT = BOARD_HEIGHT;


    public static final int BOARD_ROWS = 14 * 2;
    public static final int BOARD_COLUMNS = 14 * 2;
    public static final int CELL_SIZE = BOARD_HEIGHT / BOARD_ROWS;
    public static final int BOARD_SIZE = 4 + BOARD_WIDTH;

    public static final int BOARD_X = 2;
    public static final int BOARD_Y = 2;

    /* NN & G */
    public static final int[] LAYER_SIZE = new int[]{24, 16, 4};

    public static final int START = -5;
    public static final int END = 5;

    public static final int PRECISION = 3;

}
