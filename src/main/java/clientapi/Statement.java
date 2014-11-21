package clientapi;

import executors.CommandExecutor;
import executors.CommandExecutorFactory;
import executors.ExecutionContext;
import executors.QueryCommandExecutor;
import sqlparser.AbstractSQLCommand;
import sqlparser.SQLParserImpl;
import sqlparser.SelectCommand;

/**
 * Created on 2014-10-23.
 */
public class Statement {

    private ExecutionContext context;

    private SQLParserImpl sqlParser = new SQLParserImpl();

    public int getNextFetchSize() {
        return context.nextFetchSize;
    }

    public void setNextFetchSize(int nextFetchSize) {
        this.context.nextFetchSize = nextFetchSize;
    }

    /**
     * Executes the given SQL SELECT statement, which returns a single ResultSet object.
     * @param sql
     * @return
     */
    public ResultSet executeQuery(String sql) throws SQLException {
        SelectCommand selectCommand = sqlParser.parseQuery(sql);
        QueryCommandExecutor queryExecutor = CommandExecutorFactory.getQueryExecutorInstance();
        return queryExecutor.executeQuery(selectCommand, context);
    }

    /**
     * Executes the given SQL statement, which may be an INSERT, UPDATE, or DELETE statement or an SQL statement that returns nothing, such as an SQL DDL statement.
     * @param sql
     * @return number of rows changed.
     */
    public void executeUpdate(String sql) throws SQLException {
        AbstractSQLCommand SQLCommand = sqlParser.parse(sql);
        CommandExecutor executor = CommandExecutorFactory.getInstance(SQLCommand.getType());
        executor.execute(SQLCommand);
    }

    public void execute(String sql) throws SQLException {
         executeUpdate(sql);
    }

}
