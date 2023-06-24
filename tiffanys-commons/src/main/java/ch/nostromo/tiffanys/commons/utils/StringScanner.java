package ch.nostromo.tiffanys.commons.utils;


import lombok.Data;

/**
 * A parsing utility for scanning through strings.
 */
@Data
public class StringScanner {

    private final String text;
    private int position;
    private int savePoint;

    /**
     * Create a StringScanner, current position = 0
     * @param text input
     */
    public StringScanner(String text) {
        this.text = text;
        this.position = 0;
    }

    /**
     * Returns true if position < text length
     *
     * @return boolean
     */
    public boolean hasMore() {
        return position < text.length();
    }

    /**
     * Does return the character at the current position
     *
     * @return char
     */
    public char peek() {
        return text.charAt(position);
    }

    /**
     * Does advance the position by one (no boundaries are checked)
     */
    public void advance() {
        position++;
    }

    /**
     * Does skip all whitespaces, returns true if position is then < text length
     *
     * @return boolean
     */
    public boolean skipWhitespace() {
        while (hasMore() && Character.isWhitespace(peek())) {
            advance();
        }
        return hasMore();
    }

    /**
     * Does save the current position (no stack)
     */
    public void save() {
        this.savePoint = position;
    }

    /**
     * Does roll back the position to the save point.
     */
    public void rollBack() {
        this.position = savePoint;
    }

    /**
     * Extract a sequence between a starting and ending character, starting at the current position +1 o. Does support
     * nested sequences if found. If no end is found, the rest of the text is returned.

     * (Scanner position is advanced).
     */
    public String extractSequence(Character startChar, Character endChar) {
        advance(); // Skip start char

        StringBuilder sequence = new StringBuilder();
        int braceDepth = 1;

        while (hasMore() && braceDepth > 0) {
            char c = peek();

            if (c == startChar) {
                braceDepth++;
                sequence.append(c);
            } else if (c == endChar) {
                braceDepth--;
                if (braceDepth > 0) {
                    sequence.append(c);
                }
            } else {
                sequence.append(c);
            }

            advance();
        }

        return sequence.toString().trim();
    }



}


