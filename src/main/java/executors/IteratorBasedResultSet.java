package executors;

import clientapi.ResultSet;
import clientapi.SQLException;
import datamodel.DataTypeValue;

import java.util.Iterator;
import java.util.List;

/**
 * Created on 2014-10-23.
 */
public class IteratorBasedResultSet<E extends DataTypeValue> implements ResultSet {

    private Iterator<List<E>> queryResultIterator;
    private List<E> currentQueryResultRecord;
    private List<String> columnLabels;

    /* On construction this result set will prefetch this number of rows from storage */
    private static final int INITIAL_FETCH_SIZE = 10;

    /* default number of rows that will be fetched from storage by this ResultSet when next() is called, and all prefetched rows have been already iterated */
    private static final int DEFAULT_NEXT_FETCH_SIZE = 20;

    public IteratorBasedResultSet(Iterator queryResultIterator,
                           List<String> columnLabels,
                           ExecutionContext context) {
        int nextFetchSize = (context!=null && (context.nextFetchSize != 0))?  context.nextFetchSize : DEFAULT_NEXT_FETCH_SIZE;
        this.queryResultIterator = new PrefetchingIterator(queryResultIterator, INITIAL_FETCH_SIZE, nextFetchSize);
        this.columnLabels = columnLabels;
    }

    /**
     * Moves the cursor froward one row from its current position. A ResultSet cursor is initially positioned before the first row; the first call to the method next makes the first row the current row; the second call makes the second row the current row, and so on.
     * When a call to the next method returns false, the cursor is positioned after the last row.
     *
     * @return true if the new current row is valid; false if there are no more rows
     */
    @Override
    public boolean next() throws SQLException {
        if (queryResultIterator.hasNext()) {
            currentQueryResultRecord = queryResultIterator.next();
            return true;
        }
        return false;
    }

    /**
     * Retrieves the value of the designated column in the current row of this ResultSet object as a boolean in the Java programming language.
     * If the designated column has a datatype of CHAR or VARCHAR and contains a "0" or has a datatype of BIT, TINYINT, SMALLINT, INTEGER or BIGINT and contains a 0, a value of false is returned. If the designated column has a datatype of CHAR or VARCHAR and contains a "1" or has a datatype of BIT, TINYINT, SMALLINT, INTEGER or BIGINT and contains a 1, a value of true is returned.
     *
     * @param columnLabel
     * @return
     */
    @Override
    public  boolean getBoolean(String columnLabel) throws SQLException {
        return getBoolean(findColumn(columnLabel));
    }

    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
        return currentQueryResultRecord.get(columnIndex).booleanValue();
    }


    @Override
    public int getInt(String columnLabel) throws SQLException {
        return getInt(findColumn(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row of this ResultSet object as an int in the Java programming language.
     * the column value; if the value is SQL NULL, the value returned is 0
     */
    @Override
    public int getInt(int columnIndex) throws SQLException {
        return currentQueryResultRecord.get(columnIndex).intValue();
    }

    @Override
    public long getLong(int columnIndex) throws SQLException {
        return currentQueryResultRecord.get(columnIndex).longValue();
    }

    @Override
    public long getLong(String columnLabel) throws SQLException {
        return getLong(findColumn(columnLabel));
    }


    /**
     * * Retrieves the value of the designated column in the current row of this ResultSet object as a String in the Java programming language.
     *
     * @param
     * @return
     */
    public String getString(int columnIndex) throws SQLException {
        return currentQueryResultRecord.get(columnIndex).toString();
    }


    public String getString(String columnLabel) throws SQLException {
        return getString(findColumn(columnLabel));
    }


    @Override
    public int findColumn(String columnLabel) {
        if (!columnLabels.contains(columnLabel)) {
            throw new SQLException("there is no column \"" + columnLabel + "\" in query results");
        }
        return columnLabels.indexOf(columnLabel);
    }

}
