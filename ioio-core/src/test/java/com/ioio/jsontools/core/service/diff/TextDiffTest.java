package com.ioio.jsontools.core.service.diff;

import com.github.difflib.algorithm.DiffException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TextDiffTest {
    private TextDiff textDiff;

    @Before
    public void init() {
        textDiff = new TextDiff();
    }

    @Test
    public void testGetDiff() throws DiffException {
        String res = textDiff.diff("ABCDELMN\nPPPP\nXXXX\nnextline\nABC\nAAAA\ntest\n", "ABCFGLMN\nSTH\nXXXX\nABC\ntest\nBBBB\n");
        assertEquals("{\"oldText\":[-1,-1,2,-1,3,-1,4],\"newText\":[-1,-1,2,4,6,-1]}", res);

        res = textDiff.diff("First line\nSecond line\nThird line", "First line\nSecond line but modified\nThird line\nOops... fourth line");
        assertEquals("{\"oldText\":[0,-1,2],\"newText\":[0,-1,2,-1]}", res);
    }

    @Test
    public void testWithBothEmptyInputs() throws DiffException {
        //Dla obu pustych tekstów nie są znajdowane różnice
        String res = textDiff.diff("", "");
        assertEquals("{\"oldText\":[],\"newText\":[]}", res);
    }

    @Test
    public void testWithOneEmptyInput() throws DiffException {
        String res = textDiff.diff("12344556\nSome letters\nAnd a new line\n", "");
        assertEquals("{\"oldText\":[-1,-1,-1],\"newText\":[]}", res);

        res = textDiff.diff("", "12344556\nSome letters\nAnd a new line\n");
        assertEquals("{\"oldText\":[],\"newText\":[-1,-1,-1]}", res);
    }

    @Test
    public void testWithSwaps() throws DiffException {
        String res = textDiff.diff("First line\nSecond line\nThird line\nFourth line", "First line\nFourth line\nSecond line\nThird line");
        assertEquals("{\"oldText\":[0,2,3,-1],\"newText\":[0,-1,1,2]}", res);

        res = textDiff.diff("First line\nFourth line\nSecond line\nThird line", "First line\nSecond line\nThird line\nFourth line");
        assertEquals("{\"oldText\":[0,-1,1,2],\"newText\":[0,2,3,-1]}", res);
    }
}