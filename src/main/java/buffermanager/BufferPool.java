package buffermanager;

/**
 * Created on 2014-10-07.
 */
class BufferPool {

    /**
     * retrieve page from buffer pool. The page must exists in buffer pool, otherwise throws InvalidArgumentException
     */
    void getPage(int pageId) {}

    /**
     * loads page into the buffer pool
     */
    void addPage(int pageId){} // + storage manager reference needed?)

    void removePage(int pageId){}


}
