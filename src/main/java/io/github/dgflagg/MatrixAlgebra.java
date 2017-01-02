package io.github.dgflagg;

import io.github.dgflagg.exceptions.DimensionsNotSimilarException;
import io.github.dgflagg.exceptions.ColumnsMustEqualRowsException;
import io.github.dgflagg.model.Matrix;
import lombok.extern.slf4j.Slf4j;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dgflagg on 11/15/16.
 */
@Slf4j
public class MatrixAlgebra {

    private MatrixAlgebra() {
        //don't want to instantiate this class
    }

    /**
     * Returns a new matrix with all of the current values multiplied by the scalar
     * @param m1 the matrix to apply the scalar to
     * @param s the scalar to apply to the matrix
     * @return
     */
    public static Matrix scalarMultiply(Matrix m1, double s) {
        log.info("multiplying matrix: " + m1.getName() + " by s = " + s);

        List<List<Double>> numbers = new ArrayList<>();

        //iterate through the rows
        for(int i = 0; i < m1.getRowCount(); i++) {

            List<Double> row = new ArrayList<>();

            //iterate through the values in the row
            for(int j = 0; j < m1.getColumnCount(); j++) {

                //multiply the value by the scalar and add to the product numbers collection
                Double product = m1.getNumber(i, j) * s;
                row.add(product);

            }

            numbers.add(row);

        }

        //build the product matrix to store the result numbers
        Matrix scalarProduct = Matrix.buildMatrix(numbers);

        return scalarProduct;
    }

    /**
     * Returns a matrix that is the sum of two provided matrices
     * @param m1
     * @param m2
     * @return
     */
    public static Matrix add(Matrix m1, Matrix m2) {
        log.info("adding matrix: " + m1.getName() + " to: " + m2.getName());

        //dimensions must be similar to add two matrices
        if(!dimensionsEqual(m1, m2)) {

            DimensionsNotSimilarException e = new DimensionsNotSimilarException("cannot add matrices - dimensions not equal");
            log.error(e.getMessage());
            throw e;

        }

        List<List<Double>> numbers = new ArrayList<>();

        //iterate through the rows
        for(int i = 0; i < m1.getRowCount(); i++) {

            List<Double> row = new ArrayList<>();

            //iterate through the values in the row
            for (int j = 0; j < m1.getColumnCount(); j++) {

                Double sum = m1.getNumber(i, j) + m2.getNumber(i, j);
                row.add(sum);

            }

            numbers.add(row);

        }

        Matrix sum = Matrix.buildMatrix(numbers);

        return sum;
    }

    public static Matrix subtract(Matrix m1, Matrix m2) {
        //TODO: implement me!

        //multiply m2 by scalar -1

        //add m1 to m2

        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * Returns the dot product of two vectors of equal dimension
     * @param v1 vector
     * @param v2 vector
     * @return
     */
    public static Double dotProduct(List<Double> v1, List<Double> v2) {
        log.info("finding the dot product of v1 and v2");

        //check that the dimensions of both vectors are similar
        if(v1.size() != v2.size()) {
            DimensionsNotSimilarException e = new DimensionsNotSimilarException("length of vectors must be equal to find dot product");
            log.error(e.getMessage());
            throw e;
        }

        Double product = 0.0;

        //iterate through each of the elements in both vectors
        for(int i = 0; i < v1.size(); i++) {

            //multiply the elements in the corresponding positions
            Double multRes = v1.get(i) * v2.get(i);
            //add the result to the product total
            product = product + multRes;

        }

        return product;
    }

    /**
     * Multiplies m1 by m2, returning a the product matrix of m1 rows by m2 columns
     * @param m1 matrix A
     * @param m2 matrix B
     * @return
     */
    public static Matrix multiply(Matrix m1, Matrix m2) {
        log.info("multiplying matrix: " + m1.getName() + " by: " + m2.getName());

        //check that m1 columns are equal to m2 rows
        if(m1.getColumnCount() != m2.getRowCount()) {
            ColumnsMustEqualRowsException e = new ColumnsMustEqualRowsException("the columns of m1 must equal the rows of m2");
            log.error(e.getMessage());
            throw e;
        }

        List<List<Double>> numbers = new ArrayList<>();

        //iterate through rows of m1
        for(int m1RowIndex = 0; m1RowIndex < m1.getRowCount(); m1RowIndex++) {

            List<Double> m1RowVector = m1.getRow(m1RowIndex);
            List<Double> row = new ArrayList<>();

            //iterate through columns of m2
            for(int m2ColIndex = 0; m2ColIndex < m2.getColumnCount(); m2ColIndex++) {

                List<Double> m2ColVector = m2.getColumn(m2ColIndex);
                //find the dot product of the vectors
                Double product = MatrixAlgebra.dotProduct(m1RowVector, m2ColVector);
                row.add(product);

            }

            numbers.add(row);

        }

        Matrix product = Matrix.buildMatrix(numbers);
        return product;
    }

    /**
     * Returns true if both matrices have a similar number of rows and columns
     * @param m1
     * @param m2
     * @return
     */
    public static boolean dimensionsEqual(Matrix m1, Matrix m2) {

        //check if the matrices have the same number of rows
        if(m1.getRowCount() != m2.getRowCount()) {
            log.warn("row count is not equal between m1: " + m1.getRowCount() + " and m2: " + m2.getRowCount());
            return false;
        }

        //check if the matrices have the same number of columns
        if(m1.getColumnCount() != m2.getColumnCount()) {
            log.warn("column count is not equal between m1: " + m1.getColumnCount() + " and m2: " + m2.getColumnCount());
            return false;
        }

        return true;

    }

    /**
     * Returns true if the matrices are equivalent
     * @param m1
     * @param m2
     * @return
     */
    public static boolean isEqual(Matrix m1, Matrix m2) {

        //check that both matrices have similar row and column dimensions
        if( (m1.getRowCount() == m2.getRowCount()) && (m1.getColumnCount() == m2.getColumnCount()) ) {

            //iterate through the ith rows of both matrices
            for(int i = 0; i < m1.getRowCount(); i++) {

                //only access each row once per row
                List<Double> m1Row = m1.getRow(i);
                List<Double> m2Row = m2.getRow(i);

                //iterate through the jth columns in the row
                for(int j = 0; j < m1.getColumnCount(); j++) {

                    //compare the values from both matrices - if not equal return false
                    if(!m1Row.get(j).equals(m2Row.get(j))) {

                        return false;

                    }

                }

            }

        } else {

            //matrices have different dimensions - and thus are not equal
            return false;

        }

        return true;
    }

}
