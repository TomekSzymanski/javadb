package buffermanager;

/**
 * Interface for all page replacement algorithms
 * http://en.wikipedia.org/wiki/Page_replacement_algorithm
 */
public interface PageReplacementPolicy <K> {

    void recordUsed(K pageId);

    K getRemovalCandidate();
}
