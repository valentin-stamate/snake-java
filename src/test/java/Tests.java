import genetic_algorithm.GeneticAlgorithm;
import neural_network.NeuralNetwork;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class Tests {

    @Test
    public void test() throws Exception {
        double[] value = new double[]{0.46546, 0.64, 3, 0.2398543};
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(0.0, 1.0, value.length);

        System.out.println(Arrays.toString(value));

        boolean[] chromosome = geneticAlgorithm.toChromosome(value);

        double[] value_1 = geneticAlgorithm.toPoint(chromosome);

        System.out.println(Arrays.toString(value_1));

    }
}
