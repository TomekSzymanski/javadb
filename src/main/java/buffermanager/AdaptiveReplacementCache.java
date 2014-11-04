package buffermanager;

/**
 * Implementation of http://en.wikipedia.org/wiki/Adaptive_replacement_cache
 */
public class AdaptiveReplacementCache<K> implements PageReplacementPolicy<K> {
    @Override
    public void recordUsed(K pageId) {
    }

    @Override
    public K getRemovalCandidate() {
        return null;
    }
}
