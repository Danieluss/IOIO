package com.ioio.jsontools.core.service.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.LinkedList;

import static com.ioio.jsontools.core.service.filter.ChildAction.DELETE;
import static com.ioio.jsontools.core.service.filter.ChildAction.LEAVE;

/**
 * Class used to traverse json structure in order to remove unwanted fields.
 *
 * @author Daniel Nowak, Kamil Piechowiak
 * @version 1.1
 * @since  2019-11-7
 */
public class JsonFilter {

    private static final String LEAF = "__leaf__";
    /**
     * Node used to filter all array elements in the same way
     */
    private static final String ARRAY = "__array__";
    /**
     * Mapper used to transform json string to JsonNode
     */
    private final ObjectMapper objectMapper;
    /**
     * Filter tree traversal strategy, defines when to delete or leave nodes (eg. blacklist or whitelist)
     */
    private final FilterStrategy filterStrategy;

    public JsonFilter(ObjectMapper objectMapper, FilterStrategy filterStrategy) {
        this.objectMapper = objectMapper;
        this.filterStrategy = filterStrategy;
    }

    public String filter(String json) throws JsonProcessingException {
        return filter(objectMapper.readTree(json), filterStrategy.getDefaultRoot());
    }

    /**
     * Method filtering json according to filterJson
     * @param json that is being filtered
     * @param filterJson filter in json format
     * @return filtered json string
     * @throws JsonProcessingException for invalid json format
     */
    public String filter(String json, String filterJson) throws JsonProcessingException {
        return filter(objectMapper.readTree(json), objectMapper.readTree(filterJson));
    }

    /**
     * Method filtering json according to filterJson
     * @param json that is being filtered
     * @param filterJson filter in json format
     * @return filtered json string
     */
    public String filter(JsonNode json, JsonNode filterJson) {
        visit(json, filterJson);
        return json.toString();
    }

    /**
     * Method visiting single node in a json structure
     * @param parent json node that is being parsed
     * @param filterParent json node in filter corresponding to the node in the main structure
     * @return whether node should be left or deleted
     */
    private ChildAction visit(JsonNode parent, JsonNode filterParent) {
        if (parent.isValueNode()) {
            return visitValueNode(parent, filterParent);
        } else if (parent.isObject()) {
            visitObjectNode((ObjectNode) parent, filterParent);
        } else if (parent.isArray()) {
            visitArrayNode((ArrayNode) parent, filterParent);
        }
        return parent.size() == 0 ? DELETE : LEAVE;
    }

    /**
     * Method visiting single array in a json structure
     * @param parent json node that is being parsed
     * @param filterParent json node in filter corresponding to the node in the main structure
     */
    private void visitArrayNode(ArrayNode parent, JsonNode filterParent) {
        if (filterParent.isArray() || filterParent.has(ARRAY)) { //what about filterParent.has(ARRAY)?
            var removalList = new LinkedList<Integer>();
            for (int i = 0; i < parent.size(); i++) {
                JsonNode filterChild;
                if (filterParent.isArray()) {
                    filterChild = filterParent.get(i);
                } else {
                    filterChild = filterParent.get(ARRAY);
                }
                if (visit(parent.get(i), filterChild) == DELETE) {
                    removalList.add(i);
                }
            }
            int removedCount = 0;
            for (var index : removalList) {
                parent.remove(index - removedCount++);
            }
        } else if (!(filterParent.isValueNode() && filterParent.booleanValue() == filterStrategy.isWhitelisted())){
            parent.removeAll();
        }
    }

    /**
     * Method visiting single object in a json structure
     * @param parent json node that is being parsed
     * @param filterParent json node in filter corresponding to the node in the main structure
     */
    private void visitObjectNode(ObjectNode parent, JsonNode filterParent) {
        if(filterParent.isValueNode()) {
            if(filterParent.booleanValue() != filterStrategy.isWhitelisted()) {
                parent.removeAll();
            }
            return;
        }
        var entries = parent.fields();
        var removalList = new LinkedList<String>();
        while (entries.hasNext()) {
            var entry = entries.next();
            var key = entry.getKey();
            var value = entry.getValue();
            if (filterParent.has(key)) {
                if (visit(value, filterParent.get(key)) == DELETE) {
                    removalList.add(key);
                }
            } else if (filterStrategy.isWhitelisted()){
                removalList.add(key);
            }
        }
        removalList.forEach(((ObjectNode) parent)::remove);
    }

    /**
     * Method visiting value node in a json structure
     * @param node json node that is being parsed
     * @param filterNode json node in filter corresponding to the node in the main structure
     * @return whether node should be left or deleted
     */
    private ChildAction visitValueNode(JsonNode node, JsonNode filterNode) {
        if (filterNode.isObject()) {
            return (filterNode.has(LEAF) && filterNode.get(LEAF).booleanValue() == filterStrategy.isWhitelisted()) ? LEAVE : DELETE;
        } else {
            return (filterNode.booleanValue() == filterStrategy.isWhitelisted()) ? LEAVE : DELETE;
        }
    }

}
