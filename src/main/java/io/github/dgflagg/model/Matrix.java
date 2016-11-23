package io.github.dgflagg.model;

import io.github.dgflagg.exceptions.IndexExceedsSizeException;
import io.github.dgflagg.exceptions.NegativeIndexException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dgflagg on 11/15/16.
 */
@Slf4j
@Data
public class Matrix {

    /**
     * The name assigned to help identify this matrix
     */
    private String name;
    /**
     * Stores all values assigned to this matrix - outer list is the rows - inner list the columns
     */
    private List<List<Double>> numbers;

    private Matrix() {
        //do not instantiate using this constructor
    }

    /**
     * Prints a string representation of the name and contents of this matrix to standard out
     */
    public void print() {

        System.out.println("name: " + this.name);

        for(List<Double> row : numbers) {

            for(Double number : row) {

                System.out.print(number + " ");

            }

            System.out.println();

        }

    }

    @Override
    public String toString() {
        return "";
    }

    /**
     * Creates an identity matrix with dimensions n x n
     * @param n rows and columns
     * @return
     */
    public static Matrix buildIdentityMatrix(int n) {
        log.info("building identify matrix when n = {}", n);

        Matrix identityMatrix = new Matrix();
        identityMatrix.setName("I" + n);

        List<List<Double>> numbers = new ArrayList<List<Double>>();

        for(int i = 1; i <= n; i++) {

            List<Double> rows = new ArrayList<Double>();

            for(int j = 1; j <= n; j++) {

                if(i == j) {

                    rows.add(1d);

                } else {

                    rows.add(0d);

                }

            }

            numbers.add(rows);
        }

        identityMatrix.setNumbers(numbers);

        return identityMatrix;
    }

    /**
     * Creates a matrix of only zeroes with m x n
     * @param m rows
     * @param n columns
     * @return
     */
    public static Matrix buildZeroMatrix(int m, int n) {
        log.info("building empty matrix when m = {} and n = {}", m, n);

        Matrix matrix = new Matrix();
        matrix.setName("zero (" + m + ", " + n + ") matrix");

        List<List<Double>> numbers = new ArrayList<List<Double>>();

        for(int row = 1; row <= m; row++) {

            List<Double> rows = new ArrayList<Double>();

            for(int column = 1; column <= n; column++) {

                rows.add(0d);

            }

            numbers.add(rows);
        }

        matrix.setNumbers(numbers);

        return  matrix;
    }

    /**
     * Returns the value at in the matrix at location i, j
     * @param i row index
     * @param j column index
     * @return
     */
    public Double getNumber(int i, int j) {
       return this.getRow(i).get(j);
    }

    /**
     * Returns the list of numbers in the ith row
     * @param i row index
     * @return
     */
    public List<Double> getRow(int i) {
        if(i < 0) {
            NegativeIndexException e = new NegativeIndexException("the value of i = " + i + " is negative - the row cannot be found");
            log.error(e.getMessage());
            throw e;
        }

        if(i >= this.getRowCount()) {
            IndexExceedsSizeException e = new IndexExceedsSizeException("the value of i = " + i + " is greater than or equal to the size: " + this.getRowCount());
            log.error(e.getMessage());
            throw e;
        }

        return this.getNumbers().get(i);
    }

    /**
     * Returns the list of numbers in the jth column
     * @param j column index
     * @return
     */
    public List<Double> getColumn(int j) {
        if(j < 0) {
            NegativeIndexException e = new NegativeIndexException("the value of j = " + j + " is negative - the column cannot be found");
            log.error(e.getMessage());
            throw e;
        }

        if(j >= this.getColumnCount()) {
            IndexExceedsSizeException e = new IndexExceedsSizeException("the value of j = " + j + " is greater than or equal to the size: " + this.getColumnCount());
            log.error(e.getMessage());
            throw e;
        }

        //TODO: column is calculated when function is called - should this be constantly maintained on mutation of...
        //TODO: ... the parent matrix? ie: currently there is a row-centric view - also maintain column-centric view
        List<Double> column = new ArrayList<>();
        for(List<Double> row : this.getNumbers()) {
            Double columnValue = row.get(j);
            column.add(columnValue);
        }
        return column;
    }

    public static Matrix buildMatrix(List<List<Double>> numbers) {
        log.info("building new matrix");

        Matrix matrix = new Matrix();
        matrix.setName("matrix");
        matrix.setNumbers(numbers);

        return matrix;
    }

    /**
     * Returns true if the matrix is square (has similar size dimensions)
     * @return
     */
    public boolean isSquare() {
        if(this.getRowCount() == this.getColumnCount()) {
            return true;
        }

        return false;
    }

    /**
     * Returns the m dimension - number of rows in this matrix
     * @return
     */
    public int getRowCount() {
        return this.getNumbers().size();
    }

    /**
     * Returns the n dimension - number of columns in the first row of this matrix
     * @return
     */
    public int getColumnCount() {
        return this.getRow(0).size();
    }

}
