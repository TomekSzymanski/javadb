package clientapi;

import executors.*;
import sqlparser.SQLParseException;
import sqlparser.SQLParser;
import sqlparser.SelectCommand;
import sqlparser.AbstractSQLCommand;

/**
 * Created on 2014-10-23.
 */
public class Statement {

    private ExecutionContext context;

    public int getNextFetchSize() {
        return context.nextFetchSize;
    }

    public void setNextFetchSize(int nextFetchSize) {
        this.context.nextFetchSize = nextFetchSize;
    }

    QueryCommandExecutor queryExecutor = CommandExecutorFactory.getQueryExecutorInstance();

    /**
     * Executes the given SQL SELECT statement, which returns a single ResultSet object.
     * @param sql
     * @return
     */
    public ResultSet executeQuery(String sql) throws SQLException {
        try {
            try {
                SelectCommand selectCommand = (SelectCommand) parse(sql);
                return queryExecutor.executeQuery(selectCommand, context);
            } catch (ClassCastException e) {
                throw new SQLException("Cannot call executeQuery method with non SELECT statement");
            }
        } catch (SQLParseException e) {
            throw new SQLException(e);
        }
    }

    /**
     * Executes the given SQL statement, which may be an INSERT, UPDATE, or DELETE statement or an SQL statement that returns nothing, such as an SQL DDL statement.
     * @param sql
     * @return number of rows changed.
     */
    public int executeUpdate(String sql) throws SQLException {
        try {
            AbstractSQLCommand SQLCommand = parse(sql);
            CommandExecutor executor = CommandExecutorFactory.getInstance(SQLCommand.getType());
            return executor.execute(SQLCommand);
        } catch (SQLParseException e) {
            throw new SQLException(e);
        }
    }

    public int execute(String sql) throws SQLException {
        return executeUpdate(sql);
    }

    private AbstractSQLCommand parse(String sql) {
        SQLParser parser = new SQLParser();
        return parser.parse(sql);
    }
}
