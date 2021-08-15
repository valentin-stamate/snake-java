package controller.controlers.genetic;

import java.util.Arrays;

public class GeneticAiUtil {

    public static double[] brainToVector(double[][][] brain, int[] layerSize) {
        int vectorSize = 0;

        for (int i = 0; i < layerSize.length - 1; i++) {
            vectorSize += layerSize[i] * layerSize[i + 1];
        }

        double[] vector = new double[vectorSize];

        int p = 0;

        for (double[][] matrix : brain) {
            for (double[] line : matrix) {
                for (double value : line) {
                    vector[p++] = value;
                }
            }
        }

        return vector;
    }

    public static double[][][] vectorToBrain(double[] vector, int[] layerSize) {
        int layers = layerSize.length;

        double[][][] brain = new double[layers - 1][][];

        int p = 0;

        for (int l = 0; l < brain.length; l++) {
            int n = layerSize[l + 1];
            int m = layerSize[l];
            brain[l] = new double[n][m];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    brain[l][i][j] = vector[p++];
                }
            }
        }

        return brain;
    }

    public static double[] rotateVector(double x, double y, int degree) {
        double[] rotatedVector = new double[2];

        double radians = toRadians(degree);

        rotatedVector[0] = Math.cos(radians) * x - Math.sin(radians) * y;
        rotatedVector[1] = Math.sin(radians) * x + Math.cos(radians) * y;

        return rotatedVector;
    }

    public static int[] rotateVectorInt(double x, double y, int degree) {
        return doubleVectorToInt(rotateVector(x, y, degree));
    }

    /* TODO: move these */
    public static double toRadians(int degrees) {
        return 1.0 * degrees * Math.PI / 180.0;
    }

    public static int[] doubleVectorToInt(double[] vector) {
        int n = vector.length;

        int[] converted = new int[n];
        double roundedConstant = 0.01;

        for (int i = 0; i < n; i++) {
            vector[i] += vector[i] < 0 ? -roundedConstant : roundedConstant;
            converted[i] = (int) vector[i];
        }

        return converted;
    }

    public static int[] reverse(int[] vector) {
        int n = vector.length;

        int[] reversed = new int[n];

        for (int i = n - 1; i >= 0; i--) {
            reversed[n - i - 1] = vector[i];
        }

        return reversed;
    }

    public static double[] reverse(double[] vector) {
        int n = vector.length;

        double[] reversed = new double[n];

        for (int i = n - 1; i >= 0; i--) {
            reversed[n - i - 1] = vector[i];
        }

        return reversed;
    }

    public static int[] crushedDiagonalsToDirection(double[] dir) {
        int[] newDir = new int[2];

        newDir[0] = dir[0] < 0 ? -1 : 1;
        newDir[1] = dir[1] < 0 ? -1 : 1;

        return newDir;
    }

}
