package ru.spbstu.telematics.korobov.lab01;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.Scanner;

public class App {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Code is in MathFunctions.java");
        System.out.println("Enter vectors' dimension:");
        int vectorsSize = -1;

        while (vectorsSize < 0) {
            while (!sc.hasNextInt()) {
                sc.nextLine();
                System.out.println("Enter positive integer value!!1!");
            }
            vectorsSize = sc.nextInt();
            sc.nextLine();
            if (vectorsSize < 0) {
                System.out.println("Incorrect vector size!!1!");
            }
        }

        RealVector v1 = new ArrayRealVector(vectorsSize);
        RealVector v2 = new ArrayRealVector(vectorsSize);

        System.out.println("Enter first vector's values:");
        fillVector(v1);
        System.out.println("Enter second vector's values:");
        fillVector(v2);

        System.out.println("V1 | V2");
        for (int i = 0; i < vectorsSize; ++i) {
            System.out.println(v1.getEntry(i) + " " + v2.getEntry(i));
        }
        System.out.print("Dot Product: V1 * V2 = ");
        System.out.println(MathFunctions.dotProduct(v1, v2));
    }

    private static void fillVector(RealVector v) {
        int valuesFilled = 0;
        while (valuesFilled < v.getDimension()) {
            while (!sc.hasNextDouble()) {
                sc.nextLine();
                System.out.println("Enter double value!!1!");
            }
            v.setEntry(valuesFilled++, sc.nextDouble());
        }
    }
}
