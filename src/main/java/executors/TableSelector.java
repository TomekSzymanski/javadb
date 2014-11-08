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

    private static Storage storage = CollectionBasedInMemoryStorage.getInstance();
    private Iterator<List<? extends DataTypeValue>> tableIterator;
    private List<String> columnLabels = new ArrayList<>();

    private ExecutionContext context;

    TableSelector(Identifier tableName, List<Identifier> columnList, ExecutionContext context) {
        tableIterator = storage.tableIterator(tableName);
        for (Identifier column : columnList) {
            columnLabels.add(column.getValue());
        }
        this.context = context;
    }

    @Override
    public ResultSet getResultSet() {
        return new IteratorBasedResultSet<>(tableIterator, columnLabels, context);
    }
}
