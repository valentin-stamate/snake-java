import controller.controlers.genetic.GeneticAiUtil;
import ga.conversion.RangeDoubleToInterval;
import neural_network.math.Matrix;
import neural_network.math.Vector;
import neural_network.weights.WeightsInit;
import neural_network.weights.XavierWeightsInit;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class Tests {
    @Test
    public void brainToVectorTest() {
        WeightsInit weightsInit = new XavierWeightsInit();

        int[] layerDimension = new int[]{4, 6, 4, 2};

        double[][][] brain = new double[layerDimension.length - 1][][];

        for(int i = 0; i < brain.length; ++i) {
            int n = layerDimension[i + 1];
            int m = layerDimension[i];
            brain[i] = weightsInit.create(n, m);
        }

        double[] vector = GeneticAiUtil.brainToVector(brain, layerDimension);
        short[] bitmap = RangeDoubleToInterval.toBitMapVector(vector, -10, 10, 3);
        double[] reconvertedVector = RangeDoubleToInterval.toDoubleVector(bitmap, -10, 10, 3);

        double[][][] convertedBrain = GeneticAiUtil.vectorToBrain(reconvertedVector, layerDimension);

        System.out.println("Brain\n");
        Matrix.print(brain);

        System.out.printf("Bitmap Length %d\n", bitmap.length);
        System.out.println("\nConverted\n");

        Matrix.print(convertedBrain);
    }

    @Test
    public void vectorRotationTest() {
        int[] vector = new int[]{0, 1};

        int[] ve = GeneticAiUtil.rotateVectorInt(vector[0], vector[1], 90);

        System.out.printf("%d %d\n", ve[0], ve[1]);
    }

    @Test
    public void snakeRotation() {
        int[] dir = new int[]{0, 1}; // it's reversed

        int[] rotation =  GeneticAiUtil.reverse(GeneticAiUtil.rotateVectorInt(dir[1], dir[0], -270));

        System.out.printf("%d %d\n", rotation[0], rotation[1]);
    }

    @Test
    public void diagonalsTest() {
        int[] dir = new int[]{0, 1}; // it's reversed

        double[] rotationDiag =  GeneticAiUtil.reverse(GeneticAiUtil.rotateVector(dir[1], dir[0], -45));
        int[] normalized = GeneticAiUtil.crushedDiagonalsToDirection(rotationDiag);

        System.out.printf("%d %d\n", normalized[0], normalized[1]);

        normalized =  GeneticAiUtil.reverse(GeneticAiUtil.rotateVectorInt(normalized[1], normalized[0], -180));

        System.out.printf("%d %d\n", normalized[0], normalized[1]);
    }

    @Test
    public void copyTest() {
        int[] v = {1, 6, 3, 4, 5};

        int[] t = new int[10];

        System.arraycopy(v, 0, t, 1, v.length);

        System.out.println(Arrays.toString(t));
    }

}
