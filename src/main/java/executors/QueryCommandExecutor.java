package executors;

import clientapi.ResultSet;
import sqlparser.SelectCommand;

/**
 * Created on 2014-11-02.
 */
public interface QueryCommandExecutor {
    public ResultSet executeQuery(SelectCommand selectSQLCommand, ExecutionContext context);
}
