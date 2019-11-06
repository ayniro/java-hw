package ru.spbstu.telematics.korobov;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import java.util.Random;
import static ru.spbstu.telematics.korobov.MathFunctions.dotProduct;

public class AppTest {
    private static final Random m_Rand = new Random();
    private static final double DOUBLE_COMPARE_EPSILON = 1e-7;

    @Rule
    public final ExpectedException m_Exception = ExpectedException.none();

    @Test
    public void dotProductDimensionsMatch() {
        for (int i = 0; i < 10000; ++i) {
            int vectorSize = m_Rand.nextInt(1000);
            RealVector v1 = getRandomDoublesVector(vectorSize);
            RealVector v2 = getRandomDoublesVector(vectorSize);

            Assert.assertEquals(v1.dotProduct(v2), dotProduct(v1, v2), DOUBLE_COMPARE_EPSILON);
        }
    }

    @Test
    public void dotProductDimensionsMismatch() {
        int maxVectorSize = 10000;

        for (int i = 0; i < 10000; ++i) {
            int firstSize = m_Rand.nextInt(maxVectorSize);
            int secondSize = firstSize;
            while (secondSize == firstSize) {
                secondSize = m_Rand.nextInt(maxVectorSize);
            }

            RealVector v1 = new ArrayRealVector(firstSize);
            RealVector v2 = new ArrayRealVector(secondSize);

            m_Exception.expect(RuntimeException.class);
            dotProduct(v1, v2);
        }
    }

    private RealVector getRandomDoublesVector(int size) {
        double[] array = new double[size];
        for (int i = 0; i < size; ++i) {
            array[i] = m_Rand.nextDouble();
        }

        return new ArrayRealVector(array);
    }
}