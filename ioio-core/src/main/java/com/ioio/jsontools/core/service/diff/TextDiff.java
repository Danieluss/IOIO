package com.ioio.jsontools.core.service.diff;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class comparing two texts
 * @author Kamil Piechowiak
 * @version 1.0
 * @since 2020-01-01
 */

public class TextDiff implements Serializable {

    /**
     * Function splitting a string on newline characters
     * @param text string to split
     * @return list consisting of consecutive lines
     */
    private List<String> stringToList(String text) {
        List<String> res = new ArrayList<>(Arrays.asList(text.split("\n")));
        if(res.size() == 1 && res.get(0).equals(""))
            return new ArrayList<>();
        return res;
    }

    /**
     * Function diffing two texts.
     * @param oldText first text to be compared
     * @param newText second text to be compared
     * @return json in a string format consisting of two arrays oldText and newText. Each element of the array corresponds to a single line in the text.
     * If it is equal to -1, then this line was not matched. Else it is equal to the index of the matched line in the second text.
     * @throws DiffException on unsuccessful diff
     */
    public String diff(String oldText, String newText) throws DiffException {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode oldArray = objectMapper.createArrayNode(), newArray = objectMapper.createArrayNode();
        List<String> oldList = stringToList(oldText);
        List<String> newList = stringToList(newText);
        Patch<String> patch = DiffUtils.diff(oldList, newList);
        List<AbstractDelta<String>> deltas = patch.getDeltas();
        for(var delta : deltas) {
            int p = delta.getSource().getPosition();
            int a = delta.getSource().getLines().size();
            int b = delta.getTarget().getLines().size();
            while(oldArray.size() < p) {
                oldArray.add(newArray.size());
                newArray.add(oldArray.size()-1);
            }
            while(a > 0) {
                oldArray.add(-1);
                a--;
            }
            while(b > 0) {
                newArray.add(-1);
                b--;
            }
        }
        while(oldArray.size() < oldList.size()) {
            oldArray.add(newArray.size());
            newArray.add(oldArray.size()-1);
        }
        ObjectNode result = objectMapper.createObjectNode();
        result.set("oldText", oldArray);
        result.set("newText", newArray);
        return result.toString();
    }
}
