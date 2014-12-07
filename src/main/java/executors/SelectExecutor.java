package executors;

import clientapi.ResultSet;
import sqlparser.SelectCommand;
import storageapi.Storage;

/**
 * Created on 2014-10-30.
 */
public class SelectExecutor implements QueryCommandExecutor {

    private Storage storage;

    SelectExecutor(Storage storage) {
        this.storage = storage;
    }

    public ResultSet executeQuery(SelectCommand selectSQLCommand, ExecutionContext context) {
        QueryAssembly assembly = QueryAssemblyFactory.getInstance(storage, selectSQLCommand, context);
        return assembly.getResultSet();
    }
}
