package buffermanager;

/**
 * Implementation of <a href="http://en.wikipedia.org/wiki/Adaptive_replacement_cache">Adaptive Replacement Cache</a> page replacement algorithm.
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
