package buffermanager;

import discstorage.PageID;

/**
 * Created on 2014-10-07.
 */
class BufferPool {

    /**
     * retrieve page from buffer pool. The page must exists in buffer pool, otherwise throws InvalidArgumentException
     * @param id
     */
    void getPage(PageID id) {}

    /**
     * loads page into the buffer pool
     */
    void addPage(PageID id){} // + storage manager reference needed?)

    void removePage(PageID id){}


}
