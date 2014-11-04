package executors;

import discstorage.CollectionBasedInMemoryStorage;
import storageapi.Storage;
import sqlparser.AbstractSQLCommand;
import systemdictionary.SystemDictionary;

/**
 * Created on 2014-10-23.
 */
public class CommandExecutorFactory {

    private static Storage storage = CollectionBasedInMemoryStorage.getInstance();
    private static SystemDictionary dictionary = SystemDictionary.getInstance();

    public static CommandExecutor getInstance(AbstractSQLCommand.Type commandType) {
        switch (commandType) {
            case InsertCommand: return new InsertExecutor(storage, dictionary);
            case CreateTableCommand: return new CreateTableExecutor(storage, dictionary);
            case DropTableCommand: return new DropTableExecutor(storage, dictionary);
            default:
                throw new IllegalArgumentException("Cannot build executor for SQL command");
        }
    }

    public static QueryCommandExecutor getQueryExecutorInstance() {
        return new SelectExecutor();
    }
}
