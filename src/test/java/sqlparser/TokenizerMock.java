package sqlparser;

import sqlparser.Tokenizer;

/**
 * Created on 2014-10-04.
 */
class TokenizerMock implements Tokenizer {

    private String[] tokens;
    private int position;

    TokenizerMock(String[] tokens ) {
        this.tokens = tokens;
    }

    @Override
    public boolean hasNext() {
        return (position < tokens.length);
    }

    @Override
    public String next() {
        return tokens[position++];
    }

    @Override
    public String peek() {
        return tokens[position];
    }
}
