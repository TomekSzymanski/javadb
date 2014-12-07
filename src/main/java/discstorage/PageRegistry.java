package discstorage;

import datamodel.Identifier;
import storageapi.DataStoreException;
import systemdictionary.SystemDictionary;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created on 2014-11-13.
 */
class PageRegistry {

    private static final int NUM_PAGES = 1000;

    private static SystemDictionary systemDictionary;

    private List<Page> allPages = new ArrayList<>();

    private static PageRegistry INSTANCE;

    private PageLoader pageLoader;

    public static PageRegistry getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Trying to use not initialized Page Registry");
        }
        return INSTANCE;
    }

    public static PageRegistry loadFromDisk(File pageRegistryFile, SystemDictionary systemDictionary, PageLoader loader) {
        if (INSTANCE != null) {
            throw new IllegalStateException("Trying to initialized PageRegistry which is already initialized");
        }

        if (pageRegistryFile.length() == 0) { // new database created, have to initialize itself as empty registry
            INSTANCE = new PageRegistry(systemDictionary, loader);
        } else { // registry file not empty -> boot itself from that file
            try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(pageRegistryFile))) {
                INSTANCE = (PageRegistry) input.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new DataStoreException(e);
            }
        }
        return INSTANCE;
    }

    private PageRegistry(SystemDictionary systemDictionary, PageLoader pageLoader) {
        PageRegistry.systemDictionary = systemDictionary;
        this.pageLoader = pageLoader;
        initializeEmptyRegistry();
    }

    private void initializeEmptyRegistry() {
        for (int i = 0; i < NUM_PAGES; i++) {
            allPages.add(new Page(i, null, pageLoader, systemDictionary));
        }
    }

    public Page getUnallocatedPage() {
        Optional<Page> nextUnallocatedPage = allPages.stream().filter(p -> p.getOwningEntityId() == null).findFirst();
        if (!nextUnallocatedPage.isPresent()) {
            throw new DataStoreException("Unable to allocate any new page");
        }
        return nextUnallocatedPage.get();
    }

    public void deallocateAllPages(Identifier entity) {
        // TODO inefficient iteration thru all pages
        allPages.stream()
                .filter(p -> ((p.getOwningEntityId() != null) && p.getOwningEntityId().equals(entity)))
                .forEach((p) -> {p.deleteAllRecords(); p.setUnallocated();});
    }

    public Page findPageWithFreeSpace(Identifier entity, int recordLength) {
        Optional<Page> nextFreePage = allPages.stream()
                    .filter(p -> ((p.getOwningEntityId() == null || p.getOwningEntityId().equals(entity))
                            && (p.freeCapacity() >= recordLength)))
                    .findFirst();
        // TODO inefficient iteration thru all pages!
        if (!nextFreePage.isPresent()) {
            throw new DataStoreException("Unable to find free page for data of size " + recordLength + " for table " + entity);
        }
        return nextFreePage.get();
    }

    /**
     * Returns iterator on all the pages belonging to that object
     * @param entity
     * @return
     */
    public List<Page> getPageList(Identifier entity) {
        // TODO inefficient iteration thru all pages
        return allPages.stream().filter(p -> ((p.getOwningEntityId() != null) && p.getOwningEntityId().equals(entity))).collect(Collectors.toList());
    }

}

