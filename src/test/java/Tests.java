import neural_network.NeuralNetwork;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class Tests {

    @Test
    public void test() throws Exception {
        NeuralNetwork neuralNetwork = new NeuralNetwork(new int[]{3, 8, 6, 4});

        System.out.println(Arrays.toString(neuralNetwork.getOutput(new double[]{0.5, 0.1, -0.3})));
    }
}
