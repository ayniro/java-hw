package ru.spbstu.telematics.korobov.lab01;

import org.apache.commons.math3.linear.RealVector;

public class MathFunctions {
    static double dotProduct(RealVector v1, RealVector v2) {
        if (v1.getDimension() != v2.getDimension()) {
            throw new IllegalArgumentException("Vector sizes are not equal");
        }

        double value = 0.0;
        for (int i = 0; i < v1.getDimension(); ++i) {
            value += v1.getEntry(i) * v2.getEntry(i);
        }

        return value;
    }
}
