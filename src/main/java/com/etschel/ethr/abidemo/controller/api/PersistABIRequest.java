package com.etschel.ethr.abidemo.controller.api;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersistABIRequest {
    private JsonNode abi;
}
