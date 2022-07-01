package com.etschel.ethr.abidemo.controller;

import com.etschel.ethr.abidemo.controller.api.ABIFunctionsResponse;
import com.etschel.ethr.abidemo.controller.api.InvokeABIFunctionRequest;
import com.etschel.ethr.abidemo.controller.api.InvokeABIFunctionResponse;
import com.etschel.ethr.abidemo.controller.api.PersistABIResponse;
import com.etschel.ethr.abidemo.impl.ABIParser;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class ABIController {

    private final ABIParser abiParser;

    @Autowired
    public ABIController(ABIParser abiParser) {
        this.abiParser = abiParser;
    }

    @PostMapping(
            value = "/abi",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersistABIResponse> saveABI(@RequestBody JsonNode abi) {
        UUID abiId = abiParser.saveABI(abi);
        return ResponseEntity.ok(PersistABIResponse.from(abiId));
    }

    @GetMapping(
            value = "/{id}/functions",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ABIFunctionsResponse findABIFunctions(@PathVariable UUID id) {
        List<String> functionNames = abiParser.findFunctionNames(id);
        return ABIFunctionsResponse.from(functionNames);
    }

    @PutMapping(
            value = "/{id}/functions/{abiFunctionName}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public InvokeABIFunctionResponse invokeABIFunctionByName(
            @PathVariable UUID id,
            @PathVariable(name = "abiFunctionName") String name,
            @RequestBody InvokeABIFunctionRequest request) {
        String hexTrx = abiParser.invokeFunctionByName(id, name, request);
        return InvokeABIFunctionResponse.from(hexTrx);
    }

}
