/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.modelinglab.utils.doc;

import java.util.Map;
import javax.annotation.Nonnegative;
import javax.annotation.concurrent.Immutable;

/**
 * Classes that implements this interface simplifies conversion from (line,column) to offset and vice versa
 *
 * @author Gonzalo Ortiz Jaureguizar (gortiz at software.imdea.org)
 */
@Immutable
public interface LineBasedDocTool {
    
    @Nonnegative
    public int getOffset(int lineNumber);    
    
    /**
     * Translates from (line,column) coordinates to offset
     * @param lineNumber the number of the line. One-based
     * @param columnNumber the number of the column. One-based
     * @return 
     */
    @Nonnegative
    public int getOffset(int lineNumber, int columnNumber);
    
    public int getLine(@Nonnegative int offset);
    
    /**
     * Translates from offset to (line, column) coordinates
     * @param offset zero-based
     * @return
     */
    public Map<LineColumnEnum, Integer> getLineAndColumn(@Nonnegative int offset);
    
    /**
     * Returns the number of columns that the i-est (one-based) line have.
     * @param lineNumber
     * @return 
     */
    public int getLineWidth(int lineNumber);
    
    public int getDocumentLength();
    
    public int getLinesLength();
    
    public static enum LineColumnEnum {
        LINE, COLUMN;
    }
    
}
