package clientapi;

import executors.CommandExecutorFactory;
import storageapi.Storage;
import systemdictionary.SystemDictionary;

/**
 * Created on 2014-12-06.
 */
public class JavaDB {

    private Storage storage;

    private SystemDictionary systemDictionary;

    private CommandExecutorFactory commandExecutorFactory;

    JavaDB() {};

    JavaDB(Storage storage) {
        this.storage = storage;
        systemDictionary = storage.getSystemDictionary();
        commandExecutorFactory = new CommandExecutorFactory(storage, systemDictionary);
    }

    SystemDictionary getSystemDictionary() {
        return systemDictionary;
    }

    CommandExecutorFactory getCommandExecutorFactory() {
        return commandExecutorFactory;
    }


    public Connection getConnection() {
        return new Connection(this);
    }

    public void close() {
        storage.close();
    }

}
