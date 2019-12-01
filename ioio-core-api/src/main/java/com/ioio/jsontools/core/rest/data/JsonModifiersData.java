package com.ioio.jsontools.core.rest.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JsonModifiersData {

    private String json;
    private List<ModifierData> modifiers;

}
