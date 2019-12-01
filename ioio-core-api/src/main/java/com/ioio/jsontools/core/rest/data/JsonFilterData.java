package com.ioio.jsontools.core.rest.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JsonFilterData {
    private String json;
    private String filter;
}
