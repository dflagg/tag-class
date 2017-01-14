package io.github.dgflagg.model;

import io.github.dgflagg.exceptions.ColumnsMustEqualRowsException;
import io.github.dgflagg.exceptions.IndexExceedsSizeException;
import io.github.dgflagg.exceptions.NegativeIndexException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

        System.out.println("name: " + this.getName());

        for(List<Double> row : this.getNumbers()) {

            for(Double number : row) {

                System.out.print(number + " ");

            }

            System.out.println();

        }

    }

    /**
     * Logs the string representation of the name and contents of this matrix to info log
     */
    public void log() {

        log.info("name: " + this.getName());

        for(List<Double> row : this.getNumbers()) {

            String rowValues = "";

            for(Double number : row) {

                rowValues = rowValues + number + " ";

            }

            log.info(rowValues);

        }

    }

    /**
     * Returns the values of the matrix in a standard format without spaces
     * @return
     */
    @Override
    public String toString() {
        return this.getNumbers().toString().replace(" ","");
    }

    /**
     * Creates an identity matrix with dimensions n x n
     * @param n rows and columns
     * @return
     */
    public static Matrix buildIdentityMatrix(int n) {
        log.info("building identity matrix when n = {}", n);

        Assert.isTrue(n > -1, "cannot create a matrix with negative dimension size");

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
     * Creates a matrix of only zeroes with m x n dimensions
     * @param m rows
     * @param n columns
     * @return
     */
    public static Matrix buildZeroMatrix(int m, int n) {
        log.info("building zero matrix when m = {} and n = {}", m, n);

        Matrix matrix = Matrix.buildValueMatrix(m, n, 0d);
        matrix.setName("zero (" + m + ", " + n + ") matrix");
        return matrix;
    }

    /**
     * Creates a matrix filled with only the specified value with dimensions m x n
     * @param m rows
     * @param n columns
     * @param value the value that is used for every number in the matrix
     * @return
     */
    public static Matrix buildValueMatrix(int m, int n, Double value) {
        log.info("building value matrix when m = {}, n = {} and value = {}", m, n, value);

        //TODO: build common function for testing dimensions and use in each build function
        Assert.isTrue(n > -1, "cannot create a matrix with negative dimension size");
        Assert.isTrue(m > -1, "cannot create a matrix with negative dimension size");

        Matrix matrix = new Matrix();
        matrix.setName("value (" + m + ", " + n + ") matrix");

        List<List<Double>> numbers = new ArrayList<List<Double>>();

        for(int row = 1; row <= m; row++) {

            List<Double> rows = new ArrayList<Double>();

            for(int column = 1; column <= n; column++) {

                rows.add(value);

            }

            numbers.add(rows);
        }

        matrix.setNumbers(numbers);
        return matrix;
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

    /**
     * Creates a new Matrix from the two dimensional array of doubles
     * @param numbers
     * @return
     */
    public static Matrix buildMatrix(List<List<Double>> numbers) {
        log.info("building new matrix");

        Matrix matrix = new Matrix();
        matrix.setName("matrix");
        matrix.setNumbers(numbers);

        return matrix;
    }

    /**
     * Creates a matrix instance from a standard csv file where the elements are separated by commas
     * and the rows are separated by new lines
     * @param file
     * @return
     */
    public static Matrix csv(String file) {
        log.info("creating matrix from csv file: {}", file);

        String CSV_RECORD_DELIMITER = ",";

        List<List<Double>> numbers = new ArrayList<>();

        String line = "";
        BufferedReader br = null;
        try {

            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {

                List<Double> row = new ArrayList<>();

                String[] elements = line.split(CSV_RECORD_DELIMITER);

                for(int i = 0; i < elements.length; i++) {

                    row.add(new Double(elements[i]));

                }

                numbers.add(row);

            }

        } catch (FileNotFoundException e) {

            log.error(e.getMessage());

        } catch (IOException e) {

            log.error(e.getMessage());

        } finally {

            if (br != null) {

                try {

                    br.close();

                } catch (IOException e) {

                    log.error(e.getMessage());

                }

            }

        }

        Matrix matrix = Matrix.buildMatrix(numbers);
        matrix.setName(file);
        return matrix;
    }

    /**
     * Converts the contents of a two dimensional string array representing the values in a matrix to a matrix instance
     * Expected format: "[[1,2,3],[4,5,6]]" where each inner square bracket grouping represents the contents of one
     * entire row and "],[" delineates between the rows
     * The entire matrix is represented on a single line surrounded by enclosing square brackets
     * Spaces and jagged matrices are not supported
     * @param strArr
     * @return
     */
    public static Matrix fromString(String strArr) {

        //represents the "],[" delineating rows in the string matrix
        final String ROW_DELIMITER = "],\\[";

        //represents the comma delineating individual values in the string matrix
        final String VALUE_DELIMITER = ",";

        //represents the start of a row
        final String BEGIN_ROW_SYMBOL = "[";

        //represents the end of a row
        final String END_ROW_SYMBOL = "]";

        //represents the start of the matrix
        final String BEGIN_MATRIX_SYMBOL = "[[";

        //represents the end of the matrix
        final String END_MATRIX_SYMBOL = "]]";

        //strip the spaces from the string
        String strippedSpaces = strArr.replace(" ","");

        //strip the surrounding square brackets
        String strippedWrapperBrackets = strippedSpaces
                .replace(BEGIN_MATRIX_SYMBOL, BEGIN_ROW_SYMBOL)
                .replace(END_MATRIX_SYMBOL, END_ROW_SYMBOL);

        //split the stripped string by the row delimiter
        String[] rows = strippedWrapperBrackets.split(ROW_DELIMITER);

        List<List<Double>> numbers = new ArrayList<>();

        //iterate through the rows of the string array
        for(String row : rows) {

            //strip the row brackets from the string and split on the comma
            String[] values = row.replace(BEGIN_ROW_SYMBOL,"").replace(END_ROW_SYMBOL,"").split(VALUE_DELIMITER);

            List<Double> numberRows = new ArrayList<>();

            //iterate through each value in this row
            for(String value : values) {

                Double number = new Double(value);
                numberRows.add(number);

            }

            numbers.add(numberRows);
        }

        Matrix matrix = Matrix.buildMatrix(numbers);
        return matrix;
    }

    /**
     * Returns a list of values from a string array by using the fromString method and only returning the first row
     * @param strArr
     * @return
     */
    public static List<Double> vectorFromString(String strArr) {
        List<Double> numbers = fromString(strArr).getRow(0);
        return numbers;
    }

    /**
     * Given a matrix and a list of coefficients - appends one coefficient value to each row in the matrix
     * @param matrix
     * @param coefficientColumnValues
     * @return
     */
    public static Matrix buildAugmentedCoefficientColumnMatrix(Matrix matrix, List<Double> coefficientColumnValues) {
        //TODO: this function probably does not belong here permanently - find me a better home
        //This is probably because this accepts a matrix and performs work on it returning a new matrix instead of operating
        //on the local matrix instance

        log.info("building augmented coefficient matrix");

        //check that the coefficient column size is equal to the existing matrix's row count
        if(coefficientColumnValues.size() != matrix.getRowCount()) {

            ColumnsMustEqualRowsException e = new ColumnsMustEqualRowsException("the column size of the coefficient column must be equal to the row size of the matrix");
            log.error(e.getMessage());
            throw e;

        }

        //counter for tracking the current value of the coefficient column list
        int i = 0;
        List<List<Double>> numbers = new ArrayList<>();

        //iterate through each of the rows in the original matrix
        for(List<Double> row : matrix.getNumbers()) {

            //iterate through each of the values in a row of the original matrix
            List<Double> numberRow = new ArrayList<>();
            for(Double number : row) {

                //add each value from the original matrix to the new matrix row
                numberRow.add(number);

            }

            //add the value of from the coefficient column to this row
            numberRow.add(coefficientColumnValues.get(i));

            //increment coefficient column value counter
            i++;

            //add each of the new rows to the new matrix
            numbers.add(numberRow);
        }

        Matrix augmentedMatrix = Matrix.buildMatrix(numbers);
        augmentedMatrix.setName("augmented_" + matrix.getName());
        return augmentedMatrix;
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
