package utils.spring;

import java.util.Random;

public class NumberUtils {

    private static final Random random = new Random();

    public static double generateRandomDouble(double leftBound, double rightBound) {

        return leftBound + (rightBound - leftBound) * random.nextDouble();
    }

    public static double generateRandomDouble(double rightBound) {

        return rightBound * random.nextDouble();
    }
}
