package io.github.dgflagg;

import io.github.dgflagg.model.Matrix;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dgflagg on 11/20/16.
 */
public class Fixtures {

    public static Matrix MATRIX() {
        int N = 3;

        List<List<Double>> numbers = new ArrayList<>();

        int index = 1;
        for(int i = 0; i < N; i++) {

            List<Double> row = new ArrayList<>();

            for(int j = 0; j < N; j++) {

                row.add(new Double(index));
                index++;

            }

            numbers.add(row);

        }

        Matrix matrix = Matrix.buildMatrix(numbers);
        return matrix;
    }

}
