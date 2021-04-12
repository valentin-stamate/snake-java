package genetic_algorithm;

import java.util.Comparator;
import java.util.List;

public interface GeneticAlgorithmMember<T> extends Comparable<T> {
    double fitness();
    double score();
    void mutate();
    void evaluate();
    List<T> makeOffspring(T t);
}
