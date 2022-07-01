package com.etschel.ethr.abidemo.impl;

import com.etschel.ethr.abidemo.controller.api.InvokeABIFunctionRequest;
import com.etschel.ethr.abidemo.controller.api.exception.EntityNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class ABIParserTest {

    @Autowired
    ABIParser abiParser;

    @Autowired
    ObjectMapper mapper;

    @Test
    void testFindFunctionNotFound() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> abiParser.findFunctionNames(UUID.randomUUID()));
    }

    @Test
    void testInvokeFunctionByNameNotFound() {
        Assertions.assertThrows(EntityNotFoundException.class, () ->
                abiParser.invokeFunctionByName(UUID.randomUUID(), "test", new InvokeABIFunctionRequest()));
    }

    @Test
    void testFindFunctionName() throws Exception {
        JsonNode jsonNode = mapper.readTree(abi);
        UUID id = abiParser.saveABI(jsonNode);
        List<String> functionNames = abiParser.findFunctionNames(id);

        Assertions.assertNotNull(functionNames);
        Assertions.assertEquals(1, functionNames.size());
        Assertions.assertEquals("approve", functionNames.get(0));
    }

    @Test
    void testInvokeFunctionByName() throws Exception {
        JsonNode jsonNode = mapper.readTree(abi);
        UUID id = abiParser.saveABI(jsonNode);

        String hex = abiParser.invokeFunctionByName(id, "approve", InvokeABIFunctionRequest.builder()
                        .to("me")
                        .param(InvokeABIFunctionRequest.MethodArguments.builder()
                                .name("_spender")
                                .value("1234")
                                .build())
                        .param(InvokeABIFunctionRequest.MethodArguments.builder()
                                .name("_value")
                                .value("4321")
                                .build())
                .build());
        Assertions.assertNotNull(hex);
        Assertions.assertTrue(hex.startsWith("f84c01010181fe80b844095e"));
        System.out.println(hex);
    }

    @Test
    void testFunctionNameSimpleInput() throws Exception {
        JsonNode jsonNode = mapper.readTree("{}");
        UUID id = abiParser.saveABI(jsonNode);
        List<String> functionNames = abiParser.findFunctionNames(id);

        Assertions.assertNotNull(functionNames);
        Assertions.assertEquals(0, functionNames.size());
    }

    private final String abi = """
            [
                {
                    "inputs": [
                        {
                            "name": "_spender",
                            "type": "address"
                        },
                        {
                            "name": "_value",
                            "type": "uint256"
                        }
                    ],
                    "name": "approve",
                    "outputs": [
                        {
                            "name": "",
                            "type": "bool"
                        }
                    ],
                    "type": "function"
                }
            ]
            """;
}
