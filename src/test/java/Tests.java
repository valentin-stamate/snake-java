import board.snake.SnakeData;
import controller.HamiltonController;
import org.junit.jupiter.api.Test;
import request.Backend;

import java.io.IOException;
import java.util.List;

public class Tests {

    @Test
    public void test() {
        HamiltonController hamiltonController = new HamiltonController(null);
        int[] path = hamiltonController.getPath();

        for (int i = 0; i < path.length; i++) {
            System.out.printf("%d %d\n", i, path[i]);
        }
    }

    @Test
    void getResponse() {
        Backend backend = new Backend();
        SnakeData[] snakeDataList = (SnakeData[]) backend.receive();

        for (SnakeData snakeData : snakeDataList) {
            System.out.println(snakeData.score);
        }
    }

    @Test
    void addScore() throws IOException {
        SnakeData snakeData = new SnakeData("Snake", 14000, "dsalkj9");

        Backend backend = new Backend();
        backend.send(snakeData);
    }

}


