package executors;

import clientapi.ResultSet;
import sqlparser.SelectCommand;

/**
 * Created on 2014-10-30.
 */
public class SelectExecutor implements QueryCommandExecutor {

    private static final int DEFAULT_INITIAL_FETCH_SIZE = 20;
    private static final int DEFAULT_NEXT_FETCH_SIZE = 10;


    public ResultSet executeQuery(SelectCommand selectSQLCommand, ExecutionContext context) {
        QueryAssembly assembly = QueryAssemblyFactory.getInstance(selectSQLCommand);
        return assembly.getResultSet();
    }
}
