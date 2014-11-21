package discstorage;

import datamodel.*;
import org.junit.Test;
import storageapi.Record;
import storageapi.RecordBuilder;
import systemdictionary.SystemDictionary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PageTest {

    private static final String testTableName = "someTable";

    private static final SystemDictionary systemDictionaryStub = mock(SystemDictionary.class);

    @Test
    public void insertPageDataAndCheckFreeSpace() {
        // given: new empty page
        Page page = new Page(1, new Identifier(testTableName), new CollectionBasedPageLoaderMock() ,systemDictionaryStub );

        // when we insert one record

        int freeCapacityBefore = page.freeCapacity();
        Record record = RecordBuilder.newRecord().number(10).varchar("tomek").varchar("scsdcscds").bool(false).build();
        int recordSize = Page.getRecordValuesAndHeaderSize(record);

        page.insertRecord(record);

        // then
        int freeCapacityAfter = page.freeCapacity();
        assertEquals(freeCapacityBefore - recordSize, freeCapacityAfter);
    }

    @Test
    public void insertPageDataAndSelect() {
        Record record = RecordBuilder.newRecord().number(10).varchar("tomek").varchar("scsdcscds").bool(false).build();
        Page page = new Page(1, new Identifier(testTableName), new CollectionBasedPageLoaderMock(), systemDictionaryStub);

        // when we insert one record
        page.insertRecord(record);

        // then we should get this one back and only this one upon select
        List<Record> recordsFetched = page.getAllRecords();
        assertEquals(1, recordsFetched.size());
        assertEquals(record, recordsFetched.get(0));
    }

    @Test
    public void insertPageDataSelectAndDeleteAllRecords() {
        Page page = new Page(1, new Identifier(testTableName), new CollectionBasedPageLoaderMock(), systemDictionaryStub);

        // when we insert one record
        Record record = RecordBuilder.newRecord().number(10).varchar("tomek").varchar("scsdcscds").bool(false).build();
        page.insertRecord(record);

        // and we delete all
        page.deleteAllRecords();

        // then we should not get any records from select
        List<Record> recordsFetched = page.getAllRecords();
        assertEquals(0, recordsFetched.size());
    }

    @Test
    public void serializeSimple() throws IOException, ClassNotFoundException {
        Identifier tableNameId = new Identifier(testTableName);
        SystemDictionary systemDictionaryMock = mock(SystemDictionary.class);

        List<Column> columns = new ArrayList<>();
        columns.add(new Column(new Identifier("whatever"), SQLDataTypeFactory.getInstance(SQLDataType.NUMBER, Arrays.asList(3))));
        columns.add(new Column(new Identifier("whatever"), SQLDataTypeFactory.getInstance(SQLDataType.VARCHAR, Arrays.asList(20))));
        columns.add(new Column(new Identifier("whatever"), SQLDataTypeFactory.getInstance(SQLDataType.VARCHAR, Arrays.asList(20))));
        columns.add(new Column(new Identifier("whatever"), SQLDataTypeFactory.getInstance(SQLDataType.BOOLEAN, Collections.emptyList())));

        when(systemDictionaryMock.getTableColumnsAsList(tableNameId)).thenReturn(columns);

        Page page = new Page(11, tableNameId, new CollectionBasedPageLoaderMock(), systemDictionaryMock);

        // when we insert one record
        Record record = RecordBuilder.newRecord().number(10).varchar("tomek").varchar("scsdcscds").bool(false).build();
        page.insertRecord(record);

        Page deserializedPage = SerializationUtils.serializeDeserialize(page);
        assertEquals(page, deserializedPage);
    }

}