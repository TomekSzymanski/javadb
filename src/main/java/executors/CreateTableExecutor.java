package executors;

import clientapi.SQLException;
import storageapi.DataStoreException;
import storageapi.Storage;
import sqlparser.CreateTableCommand;
import sqlparser.AbstractSQLCommand;
import systemdictionary.DataDictionaryException;
import systemdictionary.SystemDictionary;

/**
 * Created on 2014-10-28.
 */
public class CreateTableExecutor implements CommandExecutor {

    private Storage storage;
    private SystemDictionary dictionary;

    public CreateTableExecutor(Storage storage, SystemDictionary dictionary) {
        this.storage = storage;
        this.dictionary = dictionary;
    }

    @Override
    public int execute(AbstractSQLCommand command) {
        CreateTableCommand createTableCommand = (CreateTableCommand)command; // TODO should we catch possible ClassCastException and rethrow as SQLException? Rather not as it would a programmer error to pass here command other than InsertComamnd
        try {
            dictionary.registerTable(createTableCommand.getTableInfo());
            storage.createTable(createTableCommand.getTableInfo().getTableName());
        } catch (DataDictionaryException | DataStoreException e) {
            throw new SQLException(e);
        }
        return 0;
    }
}
