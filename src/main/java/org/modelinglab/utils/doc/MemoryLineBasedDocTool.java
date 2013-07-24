/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.modelinglab.utils.doc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;
import javax.annotation.Nonnegative;
import org.modelinglab.utils.doc.LineBasedDocTool.LineColumnEnum;

/**
 * This class is a {@link LineBasedDocTool} whose methods works in constant time. This class stores
 * an integer array whose length is equal to number of lines.
 * <p/>
 * @author Gonzalo Ortiz Jaureguizar (gortiz at software.imdea.org)
 */
public class MemoryLineBasedDocTool implements Serializable, LineBasedDocTool {

    private static final long serialVersionUID = 1L;
    /**
     * Stores the offset of the last character (usually \n) of each line.
     * 
     * <ol>
     * <li>linesOffsets[i-1] + 1 is the offset where line i (one-based) starts.
     * <li>linesOffsets[i] is the offset where line i (one-based) ends.
     * </ol>
     * 
     * Offset of the first line (which corresponds with number 1) is 0, so linesOffsets[0] = -1
     */
    private Integer[] linesOffsets;

    public MemoryLineBasedDocTool(Reader r) throws IOException {
        BufferedReader buf;
        if (r instanceof BufferedReader) {
            buf = (BufferedReader) r;
        } else {
            buf = new BufferedReader(r);
        }

        /*
         * this list has the same semantic than linesOffsets
         */
        LinkedList<Integer> offsets = new LinkedList<>();

        offsets.addLast(-1); //first element is always -1, see MemoryLineBasedDocTool#linesOffsets
        
        String line = buf.readLine(); //line does not include the final \n
        while (line != null) {
            offsets.addLast(offsets.getLast() + line.length() + 1); //+1 to include \n character
            
            line = buf.readLine();
        }
        
        linesOffsets = new Integer[offsets.size()];
        linesOffsets = offsets.toArray(linesOffsets);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @Nonnegative
    public int getOffset(int lineNumber) {
        return getOffset(lineNumber, 1);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @Nonnegative
    public int getOffset(int lineNumber, int columnNumber) {
        assert lineNumber >= 1 : "Lines have to start in 1 but is " + lineNumber;
        
        return linesOffsets[lineNumber - 1] + columnNumber;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getLine(@Nonnegative int offset) {
        assert offset >= 0 : "Offset has to be equal or greater that 0. Passed is " + offset;
        
        if (offset > getDocumentLength()) {
            throw new IllegalArgumentException("Offset (" + offset + ") is outside the document (which ends in " + getDocumentLength() + ")");
        }
        
        int pos = Arrays.binarySearch(linesOffsets, offset);

        if (pos < 0) { //see Arrays#binarySearch
            pos = -pos -1;
        }
        assert pos <= linesOffsets.length && pos >= 1;

        return pos;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Map<LineColumnEnum, Integer> getLineAndColumn(@Nonnegative int offset) {
        int line = getLine(offset);
        int startOffset = getOffset(line);
        int column = offset - startOffset + 1;

        EnumMap<LineColumnEnum, Integer> result = new EnumMap<LineColumnEnum, Integer>(LineColumnEnum.class);
        result.put(LineColumnEnum.LINE, line);
        result.put(LineColumnEnum.COLUMN, column);

        return result;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getLineWidth(int lineNumber) {
        assert lineNumber >= 1 : "Lines have to start in 1 and line passed is " + lineNumber;
        return linesOffsets[lineNumber] - linesOffsets[lineNumber - 1];
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getDocumentLength() {
        if (linesOffsets.length == 0) {
            return 0;
        }
        return linesOffsets[linesOffsets.length - 1];
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getLinesLength() {
        return linesOffsets.length - 1;
    }
}
