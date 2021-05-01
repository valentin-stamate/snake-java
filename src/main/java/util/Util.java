package util;

public abstract class Util {
    public static int generateRandom(int start, int end) {
        return (((int)(Math.random() * 10000)) % (end - start)) + start;
    }
}
