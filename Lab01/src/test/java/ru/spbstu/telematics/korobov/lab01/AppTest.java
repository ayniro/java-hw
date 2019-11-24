package ru.spbstu.telematics.korobov.lab01;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Random;

import static ru.spbstu.telematics.korobov.lab01.MathFunctions.dotProduct;

public class AppTest {
    private static final Random random = new Random();
    private static final double DOUBLE_COMPARE_EPSILON = 1e-7;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void dotProductDimensionsMatch() {
        for (int i = 0; i < 10000; ++i) {
            int vectorSize = random.nextInt(1000);
            RealVector v1 = getRandomDoublesVector(vectorSize);
            RealVector v2 = getRandomDoublesVector(vectorSize);

            Assert.assertEquals(v1.dotProduct(v2), dotProduct(v1, v2), DOUBLE_COMPARE_EPSILON);
        }
    }

    @Test
    public void dotProductDimensionsMismatch() {
        int maxVectorSize = 10000;

        for (int i = 0; i < 10000; ++i) {
            int firstSize = random.nextInt(maxVectorSize);
            int secondSize = firstSize;
            while (secondSize == firstSize) {
                secondSize = random.nextInt(maxVectorSize);
            }

            RealVector v1 = new ArrayRealVector(firstSize);
            RealVector v2 = new ArrayRealVector(secondSize);

            expectedException.expect(RuntimeException.class);
            dotProduct(v1, v2);
        }
    }

    private RealVector getRandomDoublesVector(int size) {
        double[] array = new double[size];
        for (int i = 0; i < size; ++i) {
            array[i] = random.nextDouble();
        }

        return new ArrayRealVector(array);
    }
}