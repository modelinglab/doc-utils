/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.modelinglab.utils.doc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Map;
import org.junit.Test;
import org.modelinglab.utils.doc.LineBasedDocTool.LineColumnEnum;

/**
 *
 * @author gortiz
 */
public class MemoryLineBasedDocToolTest {

    public MemoryLineBasedDocToolTest() {
    }

    @Test
    public void testCreation() throws Exception {
        ArrayList<Integer> expectedOffsets;
        MemoryLineBasedDocTool tool;

        try (BufferedReader expectedOffsetsReader = new BufferedReader(
                new InputStreamReader(MemoryLineBasedDocTool.class.getResourceAsStream("getOffset.offsets")))) {


            expectedOffsets = new ArrayList<>(10);

            expectedOffsets.add(-1);

            String line = expectedOffsetsReader.readLine();
            while (line != null) {
                Integer offset = Integer.parseInt(line);
                expectedOffsets.add(offset);

                line = expectedOffsetsReader.readLine();
            }


        }

        try (Reader exampleReader = new InputStreamReader(MemoryLineBasedDocTool.class.getResourceAsStream("getOffset.txt"))) {
            tool = new MemoryLineBasedDocTool(exampleReader);
        }

        for (int line = 1; line < expectedOffsets.size(); line++) {
            assert tool.getOffset(line) == expectedOffsets.get(line) : tool.getOffset(line) + " != " + expectedOffsets.get(line);
        }
    }

    @Test
    public void testCoherence() throws Exception {

        String[] coherenceExamples = {"coherence1.txt",
                                      "coherence2.txt"};
        
        for (final String example : coherenceExamples) {
            try (Reader r = new InputStreamReader(MemoryLineBasedDocToolTest.class.getResourceAsStream(example))) {
                
                MemoryLineBasedDocTool tool = new MemoryLineBasedDocTool(r);
                
                final int max = tool.getDocumentLength();
                for (int offset = 0; offset <= max; offset++) {
                    Map<LineColumnEnum, Integer> lineAndColumn = tool.getLineAndColumn(offset);
                    int line = lineAndColumn.get(LineColumnEnum.LINE);
                    int col = lineAndColumn.get(LineColumnEnum.COLUMN);
                    assert offset == tool.getOffset(line, col) : offset + " != " + tool.getOffset(line, col);
                }
                
            }
        }


    }
}