package com.etschel.ethr.abidemo.controller;

import com.etschel.ethr.abidemo.controller.api.ABIFunctionsResponse;
import com.etschel.ethr.abidemo.controller.api.PersistABIResponse;
import com.etschel.ethr.abidemo.impl.ABIParser;
import com.etschel.ethr.abidemo.persistence.InMemoryRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class ABIController {

    private final InMemoryRepository holder;

    private final ABIParser abiParser;

    @Autowired
    public ABIController(InMemoryRepository holder, ABIParser abiParser) {
        this.holder = holder;
        this.abiParser = abiParser;
    }

    @PostMapping(value = "/abi", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersistABIResponse> saveABI(@RequestBody JsonNode abi) {
        UUID abiId = holder.saveABI(abi);
        return ResponseEntity.ok(new PersistABIResponse(abiId));
    }

    @GetMapping("/{id}/functions")
    public ABIFunctionsResponse findABIFunctions(@PathVariable UUID id) {
        return ABIFunctionsResponse.from(abiParser.findFunctionNames(id));
    }

    @PutMapping("/{id}/functions/{abiFunctionName}")
    public String invokeABIFunctionByName(@PathVariable UUID id, @PathVariable(name = "abiFunctionName") String name) {

        return null;
    }

}
