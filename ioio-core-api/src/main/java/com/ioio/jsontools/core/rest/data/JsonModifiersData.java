package com.ioio.jsontools.core.rest.data;

import com.ioio.jsontools.core.rest.AvailableModifier;
import lombok.Data;

import java.util.List;

@Data
public class JsonModifiersData {

    private String json;
    private List<AvailableModifier> modifiers;
    private List<String> params;

}
