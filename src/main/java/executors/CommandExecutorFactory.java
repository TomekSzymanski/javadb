package executors;

import storageapi.CollectionBasedInMemoryStorage;
import storageapi.Storage;
import sqlparser.AbstractSQLCommand;
import systemdictionary.SystemDictionary;

/**
 * Created on 2014-10-23.
 */
public class CommandExecutorFactory {

    private static Storage storage = CollectionBasedInMemoryStorage.getInstance();
    private static SystemDictionary dictionary = SystemDictionary.getInstance();

    public static CommandExecutor<? extends AbstractSQLCommand> getInstance(AbstractSQLCommand.Type commandType) { // TODO: cache those instances
        switch (commandType) {
            case InsertCommand: return new InsertExecutor(storage, dictionary); // TODO make system dictionary singleton, do not need to pass references to it.
            case CreateTableCommand: return new CreateTableExecutor(storage, dictionary);
            case DeleteCommand: return new DeleteTableExecutor(storage);
            case DropTableCommand: return new DropTableExecutor(storage, dictionary);
            default:
                throw new IllegalArgumentException("Cannot build executor for SQL command");
        }
    }

    public static QueryCommandExecutor getQueryExecutorInstance() {
        return new SelectExecutor();
    }
}
