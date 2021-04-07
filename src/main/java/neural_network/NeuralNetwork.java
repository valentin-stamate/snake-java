package neural_network;

import java.util.Arrays;
import java.util.Random;

public class NeuralNetwork {

    private final int[] layerDimension;
    private final int layerNumber;

    double[][][] brain;

    public NeuralNetwork(int[] layerDimension) {
        this.layerDimension = layerDimension;
        this.layerNumber = layerDimension.length;

        initializeNeuralNetwork();
    }

    private void initializeNeuralNetwork() {
        brain = new double[layerNumber][][];

        for (int i = 0; i < layerNumber - 1; i++) {
            brain[i] = new double[layerDimension[i + 1]][layerDimension[i]];
            initializeWeights(brain[i]);
        }
    }

    private void initializeWeights(double[][] layer) {
        int n = layer.length;
        int m = layer[0].length;

        double xavierInterval = Math.pow(m, -0.5);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                /* Xavier Weight Initialization */
                layer[i][j] = generateRandom(-xavierInterval, xavierInterval);
            }
        }
    }

    private double generateRandom(double start, double end) {
        Random random = new Random();
        return start + (end - start) * random.nextDouble();
    }

    public double[] getOutput(double[] input) throws Exception {
        if (input.length != layerDimension[0]) {
            throw new Exception("Input should match input dimension");
        }

        double[] currentInput = Arrays.copyOf(input, input.length);

        for (int i = 0; i < layerNumber - 1; i++) {
            currentInput = getLayerOutput(currentInput, brain[i]);
        }

        return currentInput;
    }

    private double[] getLayerOutput(double[] currentInput, double[][] layer) {
        int n = layer.length;
        int m = layer[0].length;

        return multiplyMatrices(layer, currentInput);
    }

    private double[] multiplyMatrices(double[][] A, double[] B) {
        int n = A.length;
        int m = A[0].length;

        double[] output = new double[n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                output[i] += A[i][j] * B[j];
            }
        }

        return output;
    }

}
