package sqlparser;

/**
 * Created on 2014-10-04.
 */
interface Tokenizer {
    boolean hasNext();

    String next();

    String peek();
}
