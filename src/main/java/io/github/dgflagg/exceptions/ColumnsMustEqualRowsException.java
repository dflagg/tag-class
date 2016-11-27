package io.github.dgflagg.exceptions;

/**
 * Created by dgflagg on 11/27/16.
 */
public class ColumnsMustEqualRowsException extends RuntimeException {
    public ColumnsMustEqualRowsException(String message) {
        super(message);
    }
}
