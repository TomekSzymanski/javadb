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

    private transient static SystemDictionary systemDictionary;

    private List<Page> allPages = new ArrayList<>();

    private static PageRegistry INSTANCE;

    public static PageRegistry getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Trying to use not initialized Page Registry");
        }
        return INSTANCE;
    }

    public static PageRegistry loadFromDisk(File pageRegistryFile, SystemDictionary systemDictionary) {
        if (INSTANCE != null) {
            throw new IllegalStateException("Trying to initialized PageRegistry which is already initialized");
        }

        if (pageRegistryFile.length() == 0) { // new database created, have to initialize itself as empty registry
            INSTANCE = new PageRegistry(systemDictionary);
        } else { // registry file not empty -> boot itself from that file
            try { // TODO: too many try? (first is try-with-resources)
                try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(pageRegistryFile))) {
                    INSTANCE = (PageRegistry) input.readObject();
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new DataStoreException(e);
            }
        }
        return INSTANCE;
    }

    private PageRegistry(SystemDictionary systemDictionary) {
        this.systemDictionary = systemDictionary;
        initializeEmptyRegistry();
    }

    private void initializeEmptyRegistry() {
        for (int i = 0; i < NUM_PAGES; i++) {
            allPages.add(new Page(i, null, systemDictionary));
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
        Optional<Page> nextFreePage = allPages.stream().filter(p -> p.freeCapacity() >= recordLength).findFirst();
        // TODO inefficient iteration thru all pages!
        if (!nextFreePage.isPresent()) {
            throw new DataStoreException("Unable to find free page for data of size " + recordLength + " belonging to " + entity);
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


//    public boolean isAtLeastOnePageAllocated(Identifier entity) {
//        // TODO inefficient iteration thru all pages, replace with map at least for top used
//        return allPages.stream().anyMatch(p -> ((p.getOwningEntityId() != null) && p.getOwningEntityId().equals(entity)));
//    }

}

