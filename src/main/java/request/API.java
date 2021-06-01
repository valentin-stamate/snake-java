package request;

import board.snake.SnakeData;

public interface API {
    void send(SnakeData payload);
    Object receive();
}
