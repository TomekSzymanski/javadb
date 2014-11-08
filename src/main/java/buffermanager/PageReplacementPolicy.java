package buffermanager;

/**
 * Interface for all <a href="http://en.wikipedia.org/wiki/Page_replacement_algorithm">page replacement algorithms</a>
 */
public interface PageReplacementPolicy <K> {

    /**
     * Records page usage (page with that pageID was accessed).
     * Every client using page replacement algorithm implementing this interface must call this method if they use the page
     * in order to let the algorithm know that the page was used).
     * @param pageId
     */
    void recordUsed(K pageId);

    /**
     * Offers the page id that is the first candidate to be removed from the page buffer.
     * //TODO ? actually marks the page as removed in its records?
     * @return id of the page that is the candidate to be removed
     */
    K getRemovalCandidate();
}
