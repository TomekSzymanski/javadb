package discstorage;

/**
 * Created on 2014-11-15.
 */
public interface PageLoader {
    Page getPageData(int pageId);

    void writePageData(Page page);
}
