package request;

import board.snake.SnakeData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.io.IOException;
import java.io.InputStream;

/* TODO, maybe change the name? */
public class Backend implements API {
    private static final String BACKEND_URL = "http://localhost:8082";

    @Override
    public void send(SnakeData payload) {
        HttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(BACKEND_URL + "/score");

        String json = String.format("{\"name\": \"%s\", \"score\": %d, \"boardSize\": \"%s\"}", payload.name, payload.score, payload.boardSize);

        try {
            StringEntity entity = new StringEntity(json);

            httpPost.setEntity(entity);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse response = httpClient.execute(httpPost);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SnakeData[] receive() {
        String requestURL = BACKEND_URL + "/scores";

        ObjectMapper mapper = new ObjectMapper();

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(requestURL);

            return client.execute(request, (httpResponse) -> {
                InputStream data = httpResponse.getEntity().getContent();
                return mapper.readValue(data, SnakeData[].class);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
