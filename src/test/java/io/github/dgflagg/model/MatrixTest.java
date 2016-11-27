package io.github.dgflagg.model;

import io.github.dgflagg.exceptions.IndexExceedsSizeException;
import io.github.dgflagg.exceptions.NegativeIndexException;
import org.junit.BeforeClass;
import org.junit.Test;

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

}
