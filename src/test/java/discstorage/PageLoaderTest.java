package discstorage;

import datamodel.*;
import org.junit.Test;
import systemdictionary.SystemDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class PageLoaderTest {
    
    private final PageLoader loader = getLoaderInstance();

    protected abstract PageLoader getLoaderInstance();

    private static final SystemDictionary systemDictionaryStub = mock(SystemDictionary.class);

    @Test
    public void simpleEmptyPageWriteRead() {
        Page originalPage = new Page(10, null, systemDictionaryStub);
        loader.writePageData(originalPage);
        Page loadedPage = loader.getPageData(originalPage.getPageId());
        assertEquals(originalPage, loadedPage);
    }

    @Test
    public void simpleNonEmptyPageWriteRead() {
        Identifier tableName = new Identifier("simpleNonEmptyPageWriteRead");
        List<List<DataTypeValue>> originalRecords = new ArrayList<>();

        originalRecords.add(RecordBuilder.createRecord("tomek", "szcscs", "TTTT"));
        originalRecords.add(RecordBuilder.createRecord("romek", "cccccc", "RRR"));

        SystemDictionary systemDictionaryMock = mock(SystemDictionary.class);

        List<Column> columns = new ArrayList<>();
        columns.add(new Column(new Identifier("whatever"), SQLDataTypeFactory.getInstance(SQLDataType.VARCHAR, Arrays.asList(new Integer[] {20})), false));
        columns.add(new Column(new Identifier("whatever"), SQLDataTypeFactory.getInstance(SQLDataType.VARCHAR, Arrays.asList(new Integer[] {20})), false));
        columns.add(new Column(new Identifier("whatever"), SQLDataTypeFactory.getInstance(SQLDataType.VARCHAR, Arrays.asList(new Integer[]{20})), false));

        when(systemDictionaryMock.getTableColumnsAsList(tableName)).thenReturn(columns);

        Page originalPage = new Page(10, tableName, originalRecords, systemDictionaryMock);
        loader.writePageData(originalPage);
        Page loadedPage = loader.getPageData(originalPage.getPageId());
        assertEquals(originalPage, loadedPage);
    }

    @Test (expected = IllegalArgumentException.class)
    public void nonEmptyBigNumberOfRecordsPageWriteRead() {
        List<List<DataTypeValue>> originalRecords = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            originalRecords.add(RecordBuilder.buildRecord().number(10).varchar("tomek").varchar("scsdcscds").bool(false).build());
        }

        Page originalPage = new Page(10, null, originalRecords, systemDictionaryStub);
        loader.writePageData(originalPage);
        Page loadedPage = loader.getPageData(originalPage.getPageId());
        assertEquals(originalPage, loadedPage);
    }

    @Test
    public void validateTheSameNumberOfRecordsSelectedAsInserted() {
        Identifier tableName = new Identifier("validateTheSameNumberOfRecordsSelectedAsInserted");
        List<List<DataTypeValue>> originalRecords = new ArrayList<>();
        final int numRecords = 30;

        for (int i = 0; i < numRecords; i++) {
            originalRecords.add(RecordBuilder.buildRecord()
                    .number(i)
                    .varchar(String.valueOf(i))
                    .varchar("scsdcscds")
                    .bool(i%2 == 0)
                    .build());
        }

        SystemDictionary systemDictionaryMock = mock(SystemDictionary.class);

        List<Column> columns = new ArrayList<>();
        columns.add(new Column(new Identifier("whatever"), SQLDataTypeFactory.getInstance(SQLDataType.NUMBER, Arrays.asList(new Integer[] {10})), false));
        columns.add(new Column(new Identifier("whatever"), SQLDataTypeFactory.getInstance(SQLDataType.VARCHAR, Arrays.asList(new Integer[] {20})), false));
        columns.add(new Column(new Identifier("whatever"), SQLDataTypeFactory.getInstance(SQLDataType.VARCHAR, Arrays.asList(new Integer[] {20})), false));
        columns.add(new Column(new Identifier("whatever"), SQLDataTypeFactory.getInstance(SQLDataType.BOOLEAN, Collections.emptyList()), false));

        when(systemDictionaryMock.getTableColumnsAsList(tableName)).thenReturn(columns);

        Page originalPage = new Page(10, tableName, originalRecords, systemDictionaryMock);
        loader.writePageData(originalPage);
        Page loadedPage = loader.getPageData(originalPage.getPageId());
        assertEquals(originalPage, loadedPage);

    }

}