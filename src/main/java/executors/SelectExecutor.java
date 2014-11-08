package executors;

import clientapi.ResultSet;
import sqlparser.SelectCommand;

/**
 * Created on 2014-10-30.
 */
public class SelectExecutor implements QueryCommandExecutor {

    public ResultSet executeQuery(SelectCommand selectSQLCommand, ExecutionContext context) {
        QueryAssembly assembly = QueryAssemblyFactory.getInstance(selectSQLCommand, context);
        return assembly.getResultSet();
    }
}
