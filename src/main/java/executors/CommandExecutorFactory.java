package executors;

import sqlparser.AbstractSQLCommand;
import storageapi.Storage;
import systemdictionary.SystemDictionary;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2014-10-23.
 */
public class CommandExecutorFactory {

    private Storage storage;

    private SystemDictionary dictionary;

    private Map<AbstractSQLCommand.Type, CommandExecutor<? extends AbstractSQLCommand>> executorInstances;

    public CommandExecutorFactory(Storage storage, SystemDictionary dictionary) {
        this.storage = storage;
        this.dictionary = dictionary;
        executorInstances = initializeExecutors();
    }

    public CommandExecutor<? extends AbstractSQLCommand> getInstance(AbstractSQLCommand.Type commandType) {
        CommandExecutor<? extends AbstractSQLCommand> executorInstance = executorInstances.get(commandType);
        if (executorInstance == null) {
            throw new IllegalArgumentException("Cannot build executor for SQL command " + commandType);
        }
        return executorInstance;
    }


    private Map<AbstractSQLCommand.Type, CommandExecutor<? extends AbstractSQLCommand>> initializeExecutors() {
        Map<AbstractSQLCommand.Type, CommandExecutor<? extends AbstractSQLCommand>> executorInstances = new HashMap<>();
        executorInstances.put(AbstractSQLCommand.Type.InsertCommand, new InsertExecutor(storage, dictionary));
        executorInstances.put(AbstractSQLCommand.Type.DeleteCommand, new DeleteTableExecutor(storage)); // TODO: why passing this instances: let srping initialize it in every class, during construction
        executorInstances.put(AbstractSQLCommand.Type.CreateTableCommand, new CreateTableExecutor(storage, dictionary));
        executorInstances.put(AbstractSQLCommand.Type.DropTableCommand, new DropTableExecutor(storage, dictionary));
        return executorInstances;
    }

    public QueryCommandExecutor getQueryExecutorInstance() {
        return new SelectExecutor(storage);
    }

}
