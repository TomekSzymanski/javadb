package sqlparser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created on 2014-10-03.
 */
public class SimpleTokenizer implements Iterator<String>,Tokenizer {

    private static final char SINGLE_QUOTE = '\'';
    private static final char ESCAPE_CHAR = '\\';
    private List<String> tokens = new ArrayList<>();
    private int position;

    // characters which are both tokens and also separate other tokens (without whitespace needed)
    private static final char[] SEPARATING_TOKENS = {',', '(', ')', '=', '!', '>', '<'};

    SimpleTokenizer(String input) {
        tokenize(input);
    }

    private void tokenize(String input) { // TODO rewrite this method, perhaps with state or state machine, it is not too complex
        // tokenize user input at object creation, put tokens into tokens List
        StringBuffer nextToken = new StringBuffer(); // accumulator for characters that will create nextToken
        boolean withinQuotedString = false; // control flag to remember that we are inside quoted string
        for (int pos = 0; pos < input.length(); pos++) {
            char nextChar = input.charAt(pos);

            if (nextChar == SINGLE_QUOTE && !withinQuotedString) {
                withinQuotedString = true;
                continue;
            }

            if (withinQuotedString && (nextChar == SINGLE_QUOTE || nextChar == ESCAPE_CHAR)) {
                if (pos + 1 >= input.length()) {// more characters in the input
                    continue;
                }
                if (input.charAt(pos + 1) == SINGLE_QUOTE) {
                    nextToken.append(input.charAt(pos + 1));
                    pos++; // WARN
                    continue;
                }
            }


            // if whitespace met, and we accumulated anything in nextToken, then emit nextToken
            if (( (!withinQuotedString && (Character.isWhitespace(nextChar) || isSeparatingToken(nextChar)))
                    || (withinQuotedString && nextChar == SINGLE_QUOTE)) && nextToken.length() > 0) {
                tokens.add(nextToken.toString());
                nextToken.delete(0, nextToken.length()); // clear accumulator
                if (withinQuotedString) {
                    withinQuotedString = false;
                }
            } else if ((!Character.isWhitespace(nextChar) && !isSeparatingToken(nextChar))
                            || withinQuotedString) { // accumulate if not whitespace and not separating token, or we are within quoted string
                nextToken.append(nextChar);
            }
            if (!withinQuotedString && isSeparatingToken(nextChar)) {
                String separatingToken = String.valueOf(nextChar);
                if (pos + 1 < input.length()) { // there are more characters in input, check if this char and next char form a two character separating token
                    char nextNextChar = input.charAt(pos + 1);
                    if (thisCharAndNextCharFormTwoCharacterSeparatingToken(nextChar, nextNextChar)) {
                        separatingToken += String.valueOf(nextNextChar);
                        pos++; // WARN: modify loop counter to skip next char from input (already consumed)
                    }
                }
                tokens.add(separatingToken); // add separating token itself as well
            }
        }
        // if there is anything in the accumulator add it as last token (end of input string is separator for last token)
        if (nextToken.length() > 0) {
            tokens.add(nextToken.toString());
            nextToken.delete(0, nextToken.length()); // clear accumulator
        }
    }

    private boolean isSeparatingToken(char nextChar) {
        for (char separatingToken : SEPARATING_TOKENS) {
            if (nextChar == separatingToken) {
                return true;
            }
        }
        return false;
    }

    /*
        the following combinations of two consecutive characters are also recognized as one token:
        !=, >=, <= (SQL operators for unequality, greater or equal, smaller or equal
     */
    private boolean thisCharAndNextCharFormTwoCharacterSeparatingToken(char first, char second) {
        return (((first == '!') || (first == '>') || (first == '<')) && (second == '='));
    }

    @Override
    public boolean hasNext() {
        return (position < tokens.size());
    }

    @Override
    public String next() {
        return tokens.get(position++);
    }

    @Override
    public String peek() {
        if (!hasNext()) {
            throw new NoSuchElementException("peek called while there are not more tokens");
        }
        return tokens.get(position);
    }




}
