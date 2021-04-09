package genetic_algorithm;

public class GeneticAlgorithm {
    private static double MUTATION_RATE = 0.02;
    private static int POPULATION_SIZE = 100;
    private static int MAX_GENERATIONS = 1000;
    private static double CROSSOVER_PROBABILITY = 0.2;

    private static int PRECISION = 5;

    private final int pointLength;
    private final int chromosomeLength;

    private boolean[] chromosome;

    private final int points;
    private final double intervalStart;
    private final double intervalEnd;
    private final double intervalLength;
    private final int maximumBitValue;

    public GeneticAlgorithm(double intervalStart, double intervalEnd, int points) {
        this.pointLength = (int)Math.ceil(Math.log(Math.pow(10, PRECISION)));
        this.points = points;
        this.chromosomeLength = points * pointLength;
        this.chromosome = new boolean[chromosomeLength];
        this.intervalStart = intervalStart;
        this.intervalEnd = intervalEnd;
        this.intervalLength = intervalEnd - intervalStart;
        this.maximumBitValue = (int)Math.pow(2, pointLength) - 1;
    }

    public double[] toPoint(boolean[] chromosome) {
        double[] X = new double[points];

        for (int i = 0; i < points; i++) {
            X[i] = chromosomeToPoint(i, chromosome);
        }

        return X;
    }

    public boolean[] toChromosome(double[] X) {
        boolean[] chromosome = new boolean[chromosomeLength];

        for (int i = 0; i < points; i++) {
            pointToBitmap(i, X[i], chromosome);
        }

        return chromosome;
    }

    double chromosomeToPoint(int position, boolean[] chromosome) {
        int bitPointValue = 0;

        int pStart = pointLength * position;
        int pEnd = pointLength * (position + 1) - 1;

        for (int i = pStart; i <= pEnd; i++) {
            bitPointValue = bitPointValue * 2 + (chromosome[i] ? 1 : 0);
        }

        return intervalStart + (1.0 * bitPointValue) * (intervalLength / maximumBitValue);
    }

    void pointToBitmap(int position, double value, boolean[] chromosome) {

        int pStart = pointLength * position;
        int pEnd = pointLength * (position + 1) - 1;

        int valueToCode = (int)(((value - intervalStart) * maximumBitValue) / intervalLength);

        for (int i = pEnd; i >= pStart; i--) {
            chromosome[i] = (valueToCode % 2 == 1);
            valueToCode /= 2;
        }
    }
}
