package io.github.dgflagg;

import io.github.dgflagg.model.Matrix;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dgflagg on 12/30/16.
 */
@Slf4j
@RestController
public class MatrixController {
    private static final int DEFAULT_MATRIX_N = 0;
    private static final int DEFAULT_MATRIX_M = 0;

    @RequestMapping("/idMatrix")
    public List<List<Double>> idMatrix(@RequestParam(value="n", defaultValue="0") String nValue) {

        int n = DEFAULT_MATRIX_N;

        try {
            n = Integer.parseInt(nValue);
        } catch (NumberFormatException e) {
            log.error("cannot parse: \"" + nValue + "\" to an integer");
            //TODO: more error handling
        }

        Matrix matrix = Matrix.buildZeroMatrix(DEFAULT_MATRIX_N,DEFAULT_MATRIX_N);
        try {
            matrix = Matrix.buildIdentityMatrix(n);
        } catch (IllegalArgumentException e) {
            log.error("cannot create a matrix with such dimensions");
            //TODO: more error handling
        }

        return matrix.getNumbers();
    }

    @RequestMapping("/zeroMatrix")
    public List<List<Double>> zeroMatrix(@RequestParam(value="m", defaultValue="0") String mValue,
                                         @RequestParam(value="n", defaultValue="0") String nValue) {
        int m = DEFAULT_MATRIX_M;
        int n = DEFAULT_MATRIX_N;

        try {
            m = Integer.parseInt(mValue);
            n = Integer.parseInt(nValue);
        } catch (NumberFormatException e) {
            log.error("cannot parse: \"" + nValue + "\" to an integer");
            //TODO: more error handling
        }

        Matrix matrix = Matrix.buildZeroMatrix(DEFAULT_MATRIX_M,DEFAULT_MATRIX_N);
        try {
            matrix = Matrix.buildZeroMatrix(m, n);
        } catch (IllegalArgumentException e) {
            log.error("cannot create a matrix with such dimensions");
            //TODO: more error handling
        }

        return matrix.getNumbers();
    }

    @RequestMapping("/scalarMult")
    public List<List<Double>> scalarMult(@RequestParam(value="s", defaultValue="1") String sValue,
                                         @RequestParam(value="m", defaultValue="[]") String mValue) {

        double s = 1;  //default scalar
        Matrix m1 = Matrix.buildZeroMatrix(DEFAULT_MATRIX_M,DEFAULT_MATRIX_N);

        //TODO: this is a mess - clean this method up

        try {
            s = Double.parseDouble(sValue);

            String strippedWrapperBrackets = mValue.substring(1, mValue.length() - 1);
            String[] rows = strippedWrapperBrackets.split("],\\[");

            List<List<Double>> numbers = new ArrayList<List<Double>>();

            for(String row : rows) {
                String[] values = row.replace("[","").replace("]","").split(",");

                List<Double> numberRows = new ArrayList<Double>();

                for(String value : values) {

                    Double number = new Double(value);
                    numberRows.add(number);

                }

                numbers.add(numberRows);

            }

            m1 = Matrix.buildMatrix(numbers);

        } catch (NumberFormatException e) {
            log.error("original error: " + e.getMessage());
            //log.error("cannot parse: \"" + sValue + "\" to an integer");
            //TODO: more error handling
        }

        Matrix scalarProduct = MatrixAlgebra.scalarMultiply(m1, s);

        return scalarProduct.getNumbers();
    }

}
