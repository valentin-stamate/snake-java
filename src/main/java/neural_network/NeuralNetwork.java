package neural_network;

import java.util.Arrays;
import java.util.Random;

public class NeuralNetwork {

    private final int[] layerDimension;
    private final int layerNumber;
    private final int vectorSize;

    double[][][] brain;

    public NeuralNetwork(int[] layerDimension) {
        this.layerDimension = layerDimension;
        this.layerNumber = layerDimension.length;

        int vectorSize = 1;
        for (int i = 0; i < layerDimension.length - 1; i++) {
            vectorSize += layerDimension[i] * layerDimension[i + 1];
        }
        this.vectorSize = vectorSize;

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

    public double[] getOutput(double[] input) {
        if (input.length != layerDimension[0]) {
            return null;
        }

        double[] currentInput = Arrays.copyOf(input, input.length);

        for (int i = 0; i < layerNumber - 1; i++) {
            currentInput = multiplyMatrices(brain[i], currentInput);
        }

        return currentInput;
    }

    private double[] multiplyMatrices(double[][] A, double[] B) {
        int n = A.length;
        int m = A[0].length;

        double[] output = new double[n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                output[i] += A[i][j] * B[j];
            }
            output[i] = normalizeOutput(output[i]);
            output[i] = activationFunction(output[i]);
        }

        return output;
    }

    public int vectorSize() {
        return vectorSize;
    }

    public static double normalizeOutput(double value) {
        return 0.01 + ((0.99 - 0.01) / 1.0) *  value;
    }

    /* Sigmoid Function */
    private double activationFunction(double value) {
        return 1.0D / (1 + Math.pow(Math.E, -value));
    }

    public void toVector(double[] buffer) {

        int offset = 0;
        for (int l = 0; l < layerDimension.length - 1; l++) {
            double[][] layer = brain[l];
            int n = layer.length;
            int m = layer[0].length;

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    buffer[offset + i * m + j] = layer[i][j];
                }
            }

            offset += n * m;
        }
    }

    public void update(double[] vector) {

        int offset = 0;
        for (int l = 0; l < layerDimension.length - 1; l++) {
            double[][] layer = brain[l];
            int n = layer.length;
            int m = layer[0].length;

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    layer[i][j] = vector[offset + i * m + j];
                }
            }
            offset += n * m;
        }
    }
}
