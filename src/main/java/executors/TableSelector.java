package executors;

import clientapi.ResultSet;
import datamodel.DataTypeValue;
import datamodel.Identifier;
import discstorage.CollectionBasedInMemoryStorage;
import storageapi.Storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created on 2014-11-02.
 */
class TableSelector implements QueryAssembly {

    static final String ASTERISK= "*"; // TODO move it all to datamodel?

    private static Storage storage = CollectionBasedInMemoryStorage.getInstance();
    private Iterator<List<? extends DataTypeValue>> tableIterator;
    private List<String> columnLabels = new ArrayList<>();

    TableSelector(Identifier tableName, List<Identifier> columnList) {
        tableIterator = storage.tableIterator(tableName);
        for (Identifier column : columnList) {
            columnLabels.add(column.getValue());
        }
    }

    @Override
    public ResultSet getResultSet() {
        return new IteratorBasedResultSet(tableIterator, columnLabels);
    }
}
