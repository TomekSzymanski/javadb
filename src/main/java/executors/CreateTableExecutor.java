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
public class CreateTableExecutor implements CommandExecutor<CreateTableCommand> {

    private Storage storage;
    private SystemDictionary dictionary;

    public CreateTableExecutor(Storage storage, SystemDictionary dictionary) {
        this.storage = storage;
        this.dictionary = dictionary;
    }

    @Override
    public void execute(CreateTableCommand command) {
        dictionary.registerTable(command.getTableInfo());
        storage.createTable(command.getTableInfo().getTableName());
    }
}
