package com.etschel.ethr.abidemo.persistence;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryRepository {

    private final Map<UUID, JsonNode> abis = new ConcurrentHashMap<>();

    public Optional<JsonNode> findByID(@NonNull UUID id) {
        return Optional.ofNullable(abis.get(id));
    }

    public UUID saveABI(@NonNull JsonNode abi) {
        UUID key = UUID.randomUUID();
        abis.put(key, abi);
        return key;
    }
}
