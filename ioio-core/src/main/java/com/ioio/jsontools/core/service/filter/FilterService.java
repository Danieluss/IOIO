package com.ioio.jsontools.core.service.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

import static com.ioio.jsontools.core.service.filter.ChildAction.DELETE;
import static com.ioio.jsontools.core.service.filter.ChildAction.LEAVE;

/**
 * Class used to traverse json structure in order to remove unwanted fields.
 *
 * @author Daniel Nowak & Kamil Piechowiak
 * @version 1.1
 * @since  2019-11-7
 */
@Service
public class FilterService {

    private static final String LEAF = "__leaf__";
    /**
     * Node used to filter all array elements in the same way
     */
    private static final String ARRAY = "__array__";
    /**
     * Mapper used to transform json string to JsonNode
     */
    private final ObjectMapper objectMapper;
    private final JsonNode whitelistRoot;

    public FilterService(ObjectMapper objectMapper, JsonNode whitelistRoot) {
        this.objectMapper = objectMapper;
        this.whitelistRoot = whitelistRoot;
    }

    public String filter(String json, Filter filter) throws JsonProcessingException {
        return filter(objectMapper.readTree(json), whitelistRoot, filter);
    }

    /**
     * Method filtering json according to filterJson
     * @param json that is being filtered
     * @param filterJson filter in json format
     * @param filter whether node should be left or deleted
     * @return filtered json string
     * @throws JsonProcessingException for invalid json format
     */
    public String filter(String json, String filterJson, Filter filter) throws JsonProcessingException {
        return filter(objectMapper.readTree(json), objectMapper.readTree(filterJson), filter);
    }

    /**
     * Method filtering json according to filterJson
     * @param json that is being filtered
     * @param filterJson filter in json format
     * @param filter whether node should be left or deleted
     * @return filtered json string
     */
    public String filter(JsonNode json, JsonNode filterJson, Filter filter) {
        visit(json, filterJson, filter);
        return json.toString();
    }

    /**
     * Method visiting single node in a json structure
     * @param parent json node that is being parsed
     * @param filterParent json node in filter corresponding to the node in the main structure
     * @param filter variable informing whether it is whitelist filtering or blacklist filtering
     * @return whether node should be left or deleted
     */
    private ChildAction visit(JsonNode parent, JsonNode filterParent, Filter filter) {
        if (parent.isValueNode()) {
            return visitValueNode(parent, filterParent, filter);
        } else if (parent.isObject()) {
            visitObjectNode((ObjectNode) parent, filterParent, filter);
        } else if (parent.isArray()) {
            visitArrayNode((ArrayNode) parent, filterParent, filter);
        }
        return parent.size() == 0 ? DELETE : LEAVE;
    }

    /**
     * Method visiting single array in a json structure
     * @param parent json node that is being parsed
     * @param filterParent json node in filter corresponding to the node in the main structure
     * @param filter variable informing whether it is whitelist filtering or blacklist filtering
     */
    private void visitArrayNode(ArrayNode parent, JsonNode filterParent, Filter filter) {
        if (filterParent.isArray() || filterParent.has(ARRAY)) { //what about filterParent.has(ARRAY)?
            var removalList = new LinkedList<Integer>();
            for (int i = 0; i < parent.size(); i++) {
                JsonNode filterChild;
                if (filterParent.isArray()) {
                    filterChild = filterParent.get(i);
                } else {
                    filterChild = filterParent.get(ARRAY);
                }
                if (visit(parent.get(i), filterChild, filter) == DELETE) {
                    removalList.add(i);
                }
            }
            int removedCount = 0;
            for (var index : removalList) {
                parent.remove(index - removedCount++);
            }
        } else if (!(filterParent.isValueNode() && filterParent.booleanValue() == filter.isWhitelisted())){
            parent.removeAll();
        }
    }

    /**
     * Method visiting single object in a json structure
     * @param parent json node that is being parsed
     * @param filterParent json node in filter corresponding to the node in the main structure
     * @param filter variable informing whether it is whitelist filtering or blacklist filtering
     */
    private void visitObjectNode(ObjectNode parent, JsonNode filterParent, Filter filter) {
        if(filterParent.isValueNode()) {
            if(filterParent.booleanValue() != filter.isWhitelisted()) {
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
                if (visit(value, filterParent.get(key), filter) == DELETE) {
                    removalList.add(key);
                }
            } else if (filter.isWhitelisted()){
                removalList.add(key);
            }
        }
        removalList.forEach(((ObjectNode) parent)::remove);
    }

    /**
     * Method visiting value node in a json structure
     * @param node json node that is being parsed
     * @param filterNode json node in filter corresponding to the node in the main structure
     * @param filter variable informing whether it is whitelist filtering or blacklist filtering
     * @return whether node should be left or deleted
     */
    private ChildAction visitValueNode(JsonNode node, JsonNode filterNode, Filter filter) {
        if (filterNode.isObject()) {
            return (filterNode.has(LEAF) && filterNode.get(LEAF).booleanValue() == filter.isWhitelisted()) ? LEAVE : DELETE;
        } else {
            return (filterNode.booleanValue() == filter.isWhitelisted()) ? LEAVE : DELETE;
        }
    }

}
