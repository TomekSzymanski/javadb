package executors;

import clientapi.SQLException;
import storageapi.Storage;
import sqlparser.DropTableCommand;
import sqlparser.AbstractSQLCommand;
import systemdictionary.DataDictionaryException;
import systemdictionary.SystemDictionary;

/**
 * Created on 2014-10-30.
 */
public class DropTableExecutor implements CommandExecutor<DropTableCommand> {
    private Storage storage;
    private SystemDictionary dictionary;

    public DropTableExecutor(Storage storage, SystemDictionary dictionary) {
        this.storage = storage;
        this.dictionary = dictionary;
    }

    @Override
    public void execute(DropTableCommand command) {
        dictionary.deregisterTable(command.getTableName());
        storage.dropTable(command.getTableName());
    }
}
