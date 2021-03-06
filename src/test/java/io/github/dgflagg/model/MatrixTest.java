package io.github.dgflagg.model;

import io.github.dgflagg.MatrixAlgebra;
import io.github.dgflagg.exceptions.ColumnsMustEqualRowsException;
import io.github.dgflagg.exceptions.IndexExceedsSizeException;
import io.github.dgflagg.exceptions.NegativeIndexException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by dgflagg on 11/15/16.
 */
public class MatrixTest {

    private static final int N = 3;
    private static Matrix IDENTITY_MATRIX;

    private static final int ROW_COUNT = 4;
    private static final int COLUMN_COUNT = 3;
    private static Matrix ZERO_MATRIX;

    @BeforeClass
    public static void setup() {
        IDENTITY_MATRIX = Matrix.buildIdentityMatrix(N);
        ZERO_MATRIX = Matrix.buildZeroMatrix(ROW_COUNT, COLUMN_COUNT);
    }

    @Test
    public void verify_getNumber() {
        Double position_0_0 = IDENTITY_MATRIX.getNumber(0, 0);
        assertThat(position_0_0, equalTo(1d));
        Double position_0_1 = IDENTITY_MATRIX.getNumber(0, 1);
        assertThat(position_0_1, equalTo(0d));
        Double position_1_0 = IDENTITY_MATRIX.getNumber(1, 0);
        assertThat(position_1_0, equalTo(0d));
        Double position_1_1 = IDENTITY_MATRIX.getNumber(1, 1);
        assertThat(position_1_1, equalTo(1d));
    }

    @Test
    public void verify_getRow() {
        List<Double> firstRow = IDENTITY_MATRIX.getRow(0);
        assertThat(firstRow.get(0), equalTo(1d));
        assertThat(firstRow.get(1), equalTo(0d));
        assertThat(firstRow.get(2), equalTo(0d));

        List<Double> secondRow = IDENTITY_MATRIX.getRow(1);
        assertThat(secondRow.get(0), equalTo(0d));
        assertThat(secondRow.get(1), equalTo(1d));
        assertThat(secondRow.get(2), equalTo(0d));

        List<Double> thirdRow = IDENTITY_MATRIX.getRow(2);
        assertThat(thirdRow.get(0), equalTo(0d));
        assertThat(thirdRow.get(1), equalTo(0d));
        assertThat(thirdRow.get(2), equalTo(1d));
    }

    @Test(expected = NegativeIndexException.class)
    public void verify_getRow_throws_NegativeIndexException_when_i_is_negative() {
        IDENTITY_MATRIX.getRow(-1);
    }

    @Test(expected = IndexExceedsSizeException.class)
    public void verify_getRow_throws_IndexExceedsSizeException_when_i_is_greater_than_index_size() {
        IDENTITY_MATRIX.getRow(3);
    }

    @Test
    public void verify_getColumn() {
        List<Double> firstColumn = IDENTITY_MATRIX.getColumn(0);
        assertThat(firstColumn.get(0), equalTo(1d));
        assertThat(firstColumn.get(1), equalTo(0d));
        assertThat(firstColumn.get(2), equalTo(0d));

        List<Double> secondColumn = IDENTITY_MATRIX.getColumn(1);
        assertThat(secondColumn.get(0), equalTo(0d));
        assertThat(secondColumn.get(1), equalTo(1d));
        assertThat(secondColumn.get(2), equalTo(0d));

        List<Double> thirdColumn = IDENTITY_MATRIX.getColumn(2);
        assertThat(thirdColumn.get(0), equalTo(0d));
        assertThat(thirdColumn.get(1), equalTo(0d));
        assertThat(thirdColumn.get(2), equalTo(1d));
    }

    @Test(expected = NegativeIndexException.class)
    public void verify_getColumn_throws_NegativeIndexException_when_i_is_negative() {
        IDENTITY_MATRIX.getColumn(-1);
    }

    @Test(expected = IndexExceedsSizeException.class)
    public void verify_getColumn_throws_IndexExceedsSizeException_when_i_is_greater_than_index_size() {
        IDENTITY_MATRIX.getColumn(3);
    }

    @Test
    public void verify_buildIdentityMatrix() {
        assertThat(IDENTITY_MATRIX.getRowCount(), equalTo(N));
        assertThat(IDENTITY_MATRIX.getColumnCount(), equalTo(N));

        for(int m = 0; m < N; m++) {
            for(int n = 0; n < N; n++) {
                if(m == n) {
                    assertThat(IDENTITY_MATRIX.getNumbers().get(m).get(n), equalTo(1d));
                } else {
                    assertThat(IDENTITY_MATRIX.getNumbers().get(m).get(n), equalTo(0d));
                }
            }
        }
    }

    @Test
    public void verify_buildZeroMatrix() {
        assertThat(ZERO_MATRIX.getRowCount(), equalTo(ROW_COUNT));
        assertThat(ZERO_MATRIX.getColumnCount(), equalTo(COLUMN_COUNT));

        for(List<Double> row : ZERO_MATRIX.getNumbers()) {
            for(Double number : row) {
                assertThat(number, equalTo(0d));
            }
        }
    }

    @Test
    public void verify_buildValueMatrix() {
        Double VALUE = 3.4;
        Matrix valueMatrix = Matrix.buildValueMatrix(ROW_COUNT, COLUMN_COUNT, VALUE);

        assertThat(valueMatrix.getRowCount(), equalTo(ROW_COUNT));
        assertThat(valueMatrix.getColumnCount(), equalTo(COLUMN_COUNT));

        for(List<Double> row : valueMatrix.getNumbers()) {
            for(Double number : row) {
                assertThat(number, equalTo(VALUE));
            }
        }
    }

    @Test
    public void verify_isSquare() {
        assertTrue(IDENTITY_MATRIX.isSquare());
        assertFalse(ZERO_MATRIX.isSquare());
    }

    @Test
    public void verify_csv() {
        Matrix m3x3 = Matrix.csv("src/test/resources/3x3-matrix.csv");
        assertThat(m3x3.getRowCount(), equalTo(3));
        assertThat(m3x3.getColumnCount(), equalTo(3));

        Matrix m4x2 = Matrix.csv("src/test/resources/4x2-matrix.csv");
        assertThat(m4x2.getRowCount(), equalTo(4));
        assertThat(m4x2.getColumnCount(), equalTo(2));

        Matrix m2x4 = Matrix.csv("src/test/resources/2x4-matrix.csv");
        assertThat(m2x4.getRowCount(), equalTo(2));
        assertThat(m2x4.getColumnCount(), equalTo(4));
    }

    @Test
    public void verify_to_csv_file() {
        Matrix m2x4 = Matrix.csv("src/test/resources/2x4-matrix.csv");
        m2x4.setName("testCsv");
        m2x4.csv();
        Matrix csvMatrix = Matrix.csv("testCsv.csv");

        File csvFile = new File("testCsv.csv");
        csvFile.delete();

        assertTrue(MatrixAlgebra.isEqual(m2x4, csvMatrix));
    }

    @Test
    public void verify_fromString() {
        Matrix m3x3 = Matrix.csv("src/test/resources/3x3-matrix.csv");
        Matrix m4x2 = Matrix.csv("src/test/resources/4x2-matrix.csv");
        Matrix m2x4 = Matrix.csv("src/test/resources/2x4-matrix.csv");

        Matrix m3x3Output = Matrix.fromString(m3x3.toString());
        Matrix m4x2Output = Matrix.fromString(m4x2.toString());
        Matrix m2x4Output = Matrix.fromString(m2x4.toString());

        assertTrue(MatrixAlgebra.isEqual(m3x3,m3x3Output));
        assertTrue(MatrixAlgebra.isEqual(m4x2,m4x2Output));
        assertTrue(MatrixAlgebra.isEqual(m2x4,m2x4Output));
    }

    @Test(expected = ColumnsMustEqualRowsException.class)
    public void verify_buildAugmentedCoefficientColumnMatrix_throws_ColumnsMustEqualRowsException_when_columns_dont_equal_rows() {
        List<Double> coefficientColumn = new ArrayList<>();
        coefficientColumn.add(1d);
        coefficientColumn.add(2d);

        Matrix.buildAugmentedCoefficientColumnMatrix(Matrix.csv("src/test/resources/3x3-matrix.csv"), coefficientColumn);
    }

    @Test
    public void verify_buildAugmentedCoefficientColumnMatrix() {
        Matrix m3x3 = Matrix.csv("src/test/resources/3x3-matrix.csv");

        List<Double> coefficientColumn = new ArrayList<>();
        coefficientColumn.add(1d);
        coefficientColumn.add(2d);
        coefficientColumn.add(3d);

        Matrix augmentedMatrix = Matrix.buildAugmentedCoefficientColumnMatrix(m3x3, coefficientColumn);

        for(int i = 0; i < augmentedMatrix.getRowCount(); i++) {
            for(int j = 0; j < m3x3.getColumnCount(); j++) {
                assertThat(augmentedMatrix.getNumber(i,j), equalTo(m3x3.getNumber(i, j)));
            }
            assertThat(augmentedMatrix.getNumber(i, m3x3.getColumnCount()), equalTo(coefficientColumn.get(i)));
        }
    }

}
