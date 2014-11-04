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
public class DropTableExecutor implements CommandExecutor {
    private Storage storage;
    private SystemDictionary dictionary;

    public DropTableExecutor(Storage storage, SystemDictionary dictionary) {
        this.storage = storage;
        this.dictionary = dictionary;
    }

    @Override
    public int execute(AbstractSQLCommand command) {
        DropTableCommand dropTableCommand = (DropTableCommand)command; // TODO should we catch possible ClassCastException and rethrow as SQLException? Rather not as it would a programmer error to pass here command other than InsertComamnd
        try {
            dictionary.deregisterTable(dropTableCommand.getTableName());
            storage.dropTable(dropTableCommand.getTableName());
        } catch (DataDictionaryException e) {
            throw new SQLException(e);
        }
        return 0;
    }
}
