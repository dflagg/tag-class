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
@RequestMapping("/matrix")
public class MatrixController {
    private static final int DEFAULT_MATRIX_N = 0;
    private static final int DEFAULT_MATRIX_M = 0;

    //TODO: add error response codes with appropriate messages for bad input

    //TODO: accept put/post request to persist a matrix to file or db

    @RequestMapping("/id")
    public List<List<Double>> id(@RequestParam(value="n", defaultValue="0") String nValue) {

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

    @RequestMapping("/zero")
    public List<List<Double>> zero(@RequestParam(value="m", defaultValue="0") String mValue,
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

    @RequestMapping("/augment")
    public List<List<Double>> augment(@RequestParam(value="m", defaultValue="[]") String mValue,
                                      @RequestParam(value="c", defaultValue="[]") String cValue) {
        List<Double> c = new ArrayList<>();
        Matrix m = Matrix.buildZeroMatrix(DEFAULT_MATRIX_M,DEFAULT_MATRIX_N);

        try {
            c = Matrix.vectorFromString(cValue);
            m = Matrix.fromString(mValue);
        } catch (NumberFormatException e) {
            log.error("original error: " + e.getMessage());
            //TODO: more error handling
        }

        Matrix augmentedMatrix = Matrix.buildAugmentedCoefficientColumnMatrix(m, c);
        return augmentedMatrix.getNumbers();
    }

    @RequestMapping("/scalar")
    public List<List<Double>> scalar(@RequestParam(value="s", defaultValue="1") String sValue,
                                         @RequestParam(value="m", defaultValue="[]") String mValue) {

        double s = 1;  //default scalar
        Matrix m1 = Matrix.buildZeroMatrix(DEFAULT_MATRIX_M,DEFAULT_MATRIX_N);

        try {
            s = Double.parseDouble(sValue);
            m1 = Matrix.fromString(mValue);
        } catch (NumberFormatException e) {
            log.error("original error: " + e.getMessage());
            //TODO: more error handling
        }

        Matrix scalarProduct = MatrixAlgebra.scalarMultiply(m1, s);
        return scalarProduct.getNumbers();
    }

    @RequestMapping("/add")
    public List<List<Double>> add(@RequestParam(value="m1", defaultValue="[]") String m1Value,
                                  @RequestParam(value="m2", defaultValue="[]") String m2Value) {

        Matrix m1 = Matrix.buildZeroMatrix(DEFAULT_MATRIX_M,DEFAULT_MATRIX_N);
        Matrix m2 = Matrix.buildZeroMatrix(DEFAULT_MATRIX_M,DEFAULT_MATRIX_N);

        try {
            m1 = Matrix.fromString(m1Value);
            m2 = Matrix.fromString(m2Value);
        } catch (NumberFormatException e) {
            log.error("original error: " + e.getMessage());
            //TODO: more error handling
        }

        Matrix sum = MatrixAlgebra.add(m1,m2);
        return sum.getNumbers();
    }

    @RequestMapping("/subtract")
    public List<List<Double>> subtract(@RequestParam(value="m1", defaultValue="[]") String m1Value,
                                       @RequestParam(value="m2", defaultValue="[]") String m2Value) {

        Matrix m1 = Matrix.buildZeroMatrix(DEFAULT_MATRIX_M,DEFAULT_MATRIX_N);
        Matrix m2 = Matrix.buildZeroMatrix(DEFAULT_MATRIX_M,DEFAULT_MATRIX_N);

        try {
            m1 = Matrix.fromString(m1Value);
            m2 = Matrix.fromString(m2Value);
        } catch (NumberFormatException e) {
            log.error("original error: " + e.getMessage());
            //TODO: more error handling
        }

        Matrix difference = MatrixAlgebra.subtract(m1,m2);
        return difference.getNumbers();
    }

    @RequestMapping("/multiply")
    public List<List<Double>> multiply(@RequestParam(value="m1", defaultValue="[]") String m1Value,
                                       @RequestParam(value="m2", defaultValue="[]") String m2Value) {

        Matrix m1 = Matrix.buildZeroMatrix(DEFAULT_MATRIX_M,DEFAULT_MATRIX_N);
        Matrix m2 = Matrix.buildZeroMatrix(DEFAULT_MATRIX_M,DEFAULT_MATRIX_N);

        try {
            m1 = Matrix.fromString(m1Value);
            m2 = Matrix.fromString(m2Value);
        } catch (NumberFormatException e) {
            log.error("original error: " + e.getMessage());
            //TODO: more error handling
        }

        Matrix product = MatrixAlgebra.multiply(m1,m2);
        return product.getNumbers();
    }

}
