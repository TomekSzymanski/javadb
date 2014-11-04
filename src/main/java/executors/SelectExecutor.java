package executors;

import clientapi.ResultSet;
import storageapi.Storage;
import sqlparser.SelectCommand;
import systemdictionary.SystemDictionary;

/**
 * Created on 2014-10-30.
 */
public class SelectExecutor implements QueryCommandExecutor {

    private static final int DEFAULT_INITIAL_FETCH_SIZE = 20;
    private static final int DEFAULT_NEXT_FETCH_SIZE = 10;

    private Storage storage;
    private SystemDictionary dictionary;


    SelectExecutor(Storage storage, SystemDictionary dictionary) {
        this.storage = storage;
        this.dictionary = dictionary;
    }


    public ResultSet executeQuery(SelectCommand selectSQLCommand, ExecutionContext context) {
        QueryAssembly assembly = QueryAssemblyFactory.getInstance(selectSQLCommand);
        return assembly.getResultSet();
    }
}
