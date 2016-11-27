package io.github.dgflagg;

import io.github.dgflagg.exceptions.ColumnsMustEqualRowsException;
import io.github.dgflagg.exceptions.DimensionsNotSimilarException;
import io.github.dgflagg.model.Matrix;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by dgflagg on 11/19/16.
 */
public class MatrixAlgebraTest {

    private static final int N = 5;

    @Test
    public void verify_isEqual() {
        Matrix m1 = Matrix.buildIdentityMatrix(N);
        Matrix m2 = Matrix.buildIdentityMatrix(N);
        Matrix m3 = Matrix.buildIdentityMatrix(N + 1);
        Matrix m4 = Matrix.buildZeroMatrix(N, N);
        Matrix m5 = Fixtures.MATRIX();

        assertTrue(MatrixAlgebra.isEqual(m1, m2));
        assertFalse(MatrixAlgebra.isEqual(m1, m3));
        assertFalse(MatrixAlgebra.isEqual(m1, m4));
        assertFalse(MatrixAlgebra.isEqual(m1, m5));
    }

    @Test
    public void verify_dimensionsEqual() {
        Matrix m1 = Matrix.buildIdentityMatrix(N);
        Matrix m2 = Matrix.buildIdentityMatrix(N);
        Matrix m3 = Fixtures.MATRIX();
        Matrix m4 = Matrix.buildZeroMatrix(N, N);
        Matrix m5 = Matrix.buildZeroMatrix(N, N + 1);
        Matrix m6 = Matrix.buildZeroMatrix(N + 1, N);

        assertTrue(MatrixAlgebra.dimensionsEqual(m1, m1));
        assertTrue(MatrixAlgebra.dimensionsEqual(m1, m2));
        assertFalse(MatrixAlgebra.dimensionsEqual(m1, m3));
        assertTrue(MatrixAlgebra.dimensionsEqual(m1, m4));
        assertFalse(MatrixAlgebra.dimensionsEqual(m4, m5));
        assertFalse(MatrixAlgebra.dimensionsEqual(m4, m6));
    }

    @Test
    public void verify_scalarMultiply() {
        Matrix m1 = Fixtures.MATRIX();
        int scalar = 2;

        Matrix product = MatrixAlgebra.scalarMultiply(m1, scalar);

        //check that all the values are in the product matrix were multiplied correctly by the scalar
        for(int i = 0; i < m1.getRowCount(); i++) {
            for(int j = 0; j < m1.getColumnCount(); j++) {
                assertThat(product.getNumber(i,j), equalTo(m1.getNumber(i,j) * scalar));
            }
        }
    }

    @Test
    public void verify_add() {
        Matrix m1 = Fixtures.MATRIX();
        Matrix m2 = Fixtures.MATRIX();

        Matrix sum = MatrixAlgebra.add(m1, m2);

        for(int i = 0; i < m1.getRowCount(); i++) {
            for(int j = 0; j < m1.getColumnCount(); j++) {
                assertThat(sum.getNumber(i, j), equalTo(m1.getNumber(i, j) + m2.getNumber(i, j)));
            }
        }
    }

    @Test(expected = DimensionsNotSimilarException.class)
    public void verify_add_throws_DimensionsNotSimilarException_when_rows_are_not_equivalent() {
        Matrix m1 = Matrix.buildZeroMatrix(N, N);
        Matrix m2 = Matrix.buildZeroMatrix(N + 1, N);

        MatrixAlgebra.add(m1,m2);
    }

    @Test(expected = DimensionsNotSimilarException.class)
    public void verify_add_throws_DimensionsNotSimilarException_when_columns_are_not_equivalent() {
        Matrix m1 = Matrix.buildZeroMatrix(N, N);
        Matrix m2 = Matrix.buildZeroMatrix(N, N + 1);

        MatrixAlgebra.add(m1,m2);
    }

    @Test(expected = DimensionsNotSimilarException.class)
    public void verify_dotProduct_throws_DimensionsNotSimilarException_when_vector_size_is_different() {
        Matrix m1 = Matrix.buildZeroMatrix(N, N + 1);
        MatrixAlgebra.dotProduct(m1.getRow(0), m1.getColumn(0));
    }

    @Test
    public void verify_dotProduct() {
        List<Double> v1 = new ArrayList<>();
        v1.add(1d);
        v1.add(2d);
        v1.add(3d);

        List<Double> v2 = new ArrayList<>();
        v2.add(-2d);
        v2.add(0d);
        v2.add(5d);

        Double product = MatrixAlgebra.dotProduct(v1, v2);
        assertThat(product, equalTo(13d));
    }

    @Test(expected = ColumnsMustEqualRowsException.class)
    public void verify_multiply_throws_ColumnsMustEqualRowsException_when_columns_do_not_equal_rows() {
        Matrix m1 = Matrix.buildZeroMatrix(N, N);
        Matrix m2 = Matrix.buildZeroMatrix(N + 1, N);
        MatrixAlgebra.multiply(m1, m2);
    }

    @Test
    public void verify_multiply() {
        Matrix A = Matrix.csv("src/test/resources/A.csv");
        Matrix B = Matrix.csv("src/test/resources/B.csv");
        Matrix AB = Matrix.csv("src/test/resources/AB.csv");

        Matrix result = MatrixAlgebra.multiply(A, B);
        assertTrue(MatrixAlgebra.isEqual(result, AB));
    }

}
