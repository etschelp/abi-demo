package com.etschel.ethr.abidemo.impl;

import com.etschel.ethr.abidemo.controller.api.exception.EntityNotFoundException;
import com.etschel.ethr.abidemo.persistence.InMemoryRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.abi.datatypes.AbiTypes;
import org.web3j.abi.datatypes.Type;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ABIParser {

    public static final TypeReference<List<AbiFunction>> ABI_FUNCTION_LIST = new TypeReference<>() {
    };


    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private InMemoryRepository holder;

    private final Gson gson = new GsonBuilder().create();

    public List<String> findFunctionNames(@NonNull UUID id) {
        JsonNode abi = holder.findByID(id).orElseThrow(EntityNotFoundException::new);
        if (abi.isEmpty()) {
            return List.of();
        }
        List<AbiFunction> abiFunctions = mapper.convertValue(abi, ABI_FUNCTION_LIST);
        return abiFunctions.stream()
                .filter(f -> "function".equals(f.getType()))
                .map(AbiFunction::getName).collect(Collectors.toList());
    }

    public Optional<AbiFunction> findFunctionByName(@NonNull UUID id, @NonNull String name) {
        return Optional.empty();
    }



    public List<AbiFunction> getFunctions(String abi) {
        JsonObject root = gson.fromJson(abi, JsonObject.class);
        JsonArray parts = root.getAsJsonArray();
        parts.forEach(el -> {
            if (el.getAsJsonObject().has("type") && el.getAsJsonObject().get("type").getAsString() == "function") {
                String name = el.getAsJsonObject().get("name").getAsString();
                Map<String, Class<? extends Type>> inputTypes = new HashMap<>();
                JsonArray input = el.getAsJsonObject().get("input").getAsJsonArray();
                input.forEach(i -> {
                    String inputName = i.getAsJsonObject().get("name").getAsString();
                    String inputType = i.getAsJsonObject().get("type").getAsString();
                    inputTypes.put(inputType, AbiTypes.getType(inputType, true));
                });
            }
        });
        return null;
    }

    @Data
    @NoArgsConstructor
    public static final class AbiFunction {
        private String name;
        private String type;
    }
}
