package ru.spbstu.telematics.korobov;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.util.Random;

import static ru.spbstu.telematics.korobov.MathFunctions.dotProduct;

public class AppTest {
    private static final Random rand = new Random();
    private static final double DOUBLE_COMPARE_EPSILON = 1e-7;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void dotProductDimensionsMatch() {
        for (int i = 0; i < 10000; ++i) {
            int vectorSize = rand.nextInt(1000);
            RealVector v1 = getRandomDoublesVector(vectorSize);
            RealVector v2 = getRandomDoublesVector(vectorSize);

            Assert.assertEquals(v1.dotProduct(v2), dotProduct(v1, v2), DOUBLE_COMPARE_EPSILON);
        }
    }

    @Test
    public void dotProductDimensionsMismatch() {
        int maxVectorSize = 10000;

        for (int i = 0; i < 10000; ++i) {
            int firstSize = rand.nextInt(maxVectorSize);
            int secondSize = firstSize;
            while (secondSize == firstSize) {
                secondSize = rand.nextInt(maxVectorSize);
            }

            RealVector v1 = new ArrayRealVector(firstSize);
            RealVector v2 = new ArrayRealVector(secondSize);

            exception.expect(RuntimeException.class);
            dotProduct(v1, v2);
        }
    }

    private RealVector getRandomDoublesVector(int size) {
        double[] array = new double[size];
        for (int i = 0; i < size; ++i) {
            array[i] = rand.nextDouble();
        }

        return new ArrayRealVector(array);
    }
}