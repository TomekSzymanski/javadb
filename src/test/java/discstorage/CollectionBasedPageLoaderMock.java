package discstorage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2014-11-20.
 */
public class CollectionBasedPageLoaderMock implements PageLoader {

    private Map<Integer, Page> pages = new HashMap<>();

    @Override
    public Page getPageData(int pageId) {
        return pages.get(pageId);
    }

    @Override
    public void writePageData(Page page) {
        pages.put(page.getPageId(), page);
    }
}
