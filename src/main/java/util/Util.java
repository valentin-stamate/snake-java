package util;

public abstract class Util {
    public static int generateRandom(int start, int end) {
        return (((int)(Math.random() * 10000)) % (end - start)) + start;
    }

    public static int getNodeCount(int i, int j, int columns) {
        return i * columns + j;
    }

    public static void getNeighbours(int[] current, int[] up, int[] right, int[] down, int[] left) {
        int n = Config.BOARD_ROWS;
        int m = Config.BOARD_COLUMNS;

        /* UP */
        up[0] = current[0] - 1;
        up[1] = current[1];

        /* RIGHT */
        right[0] = current[0];
        right[1] = current[1] + 1;

        /* DOWN */
        right[0] = current[0] + 1;
        right[1] = current[1];

        /* LEFT */
        right[0] = current[0];
        right[1] = current[1] - 1;

        /* UP RESET */
        if (current[0] == 0) {
            up[0] = 0;
            up[1] = 0;
        }

        if (current[0] == Config.BOARD_ROWS - 1) {
            down[0] = 0;
            down[1] = 0;
        }

        if (current[1] == 0) {
            right[0] = 0;
            right[1] = 0;
        }

        if (current[1] == Config.BOARD_COLUMNS - 1) {
            left[0] = 0;
            left[1] = 0;
        }
    }

    public static int distanceBetween(int[] a, int[] b) {
        return (int)Math.sqrt(Math.pow(a[0] - b[0], 2) + Math.pow(a[1] - b[1], 2));
    }
}
