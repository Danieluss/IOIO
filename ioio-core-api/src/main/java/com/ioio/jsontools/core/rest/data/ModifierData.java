package com.ioio.jsontools.core.rest.data;

import com.ioio.jsontools.core.rest.ModifierType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ModifierData {

    public ModifierType type;
    public String params;

}
