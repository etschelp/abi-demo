package com.etschel.ethr.abidemo.impl;

import com.etschel.ethr.abidemo.controller.api.InvokeABIFunctionRequest;
import com.etschel.ethr.abidemo.controller.api.exception.EntityNotFoundException;
import com.etschel.ethr.abidemo.persistence.InMemoryRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;

import javax.inject.Inject;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ABIParser {

    public static final TypeReference<List<AbiFunction>> ABI_FUNCTION_LIST = new TypeReference<>() {
    };

    private final ObjectMapper mapper;

    private final InMemoryRepository holder;

    @Inject
    public ABIParser(ObjectMapper mapper, InMemoryRepository holder) {
        this.mapper = mapper;
        this.holder = holder;
    }

    public UUID saveABI(@NonNull JsonNode abi) {
        return holder.saveABI(abi);
    }

    public List<String> findFunctionNames(@NonNull UUID id) {
        return findFunctions(id)
                .stream()
                .map(AbiFunction::getName)
                .collect(Collectors.toList());
    }

    public String invokeFunctionByName(@NonNull UUID id, @NonNull String name, @NonNull InvokeABIFunctionRequest request) {
        AbiFunction abiFunction = findFunctionByName(id, name).orElseThrow(EntityNotFoundException::new);

        // input types
        List<Type> inputTypes = new ArrayList<>();
        request.getParams().forEach(param -> {
            String type = findTypeForName(abiFunction, param.getName());
            if ("uint256".equals(type)) {
                inputTypes.add(new Uint256(new BigInteger(param.getValue())));
            } else if ("address".equals(type)) {
                inputTypes.add(new Address(param.getValue()));
            }
        });

        // output types
        List<org.web3j.abi.TypeReference<?>> outputTypes = new ArrayList<>();
        abiFunction.getOutputs().forEach(in -> {
            try {
                outputTypes.add(org.web3j.abi.TypeReference.makeTypeReference(in.getType()));
            } catch (ClassNotFoundException e) {
                throw new EntityNotFoundException();
            }
        });

        Function func = new Function(
                abiFunction.getName(),
                inputTypes,
                outputTypes);

        String encodedFunction = FunctionEncoder.encode(func);
        RawTransaction transaction = RawTransaction
                .createTransaction(BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, request.getTo(), encodedFunction);
        byte[] encode = TransactionEncoder.encode(transaction);
        return Hex.encodeHexString(encode);
    }

    private List<AbiFunction> findFunctions(@NonNull UUID id) {
        JsonNode abi = holder.findByID(id).orElseThrow(EntityNotFoundException::new);
        if (abi.isEmpty()) {
            return List.of();
        }
        List<AbiFunction> abiFunctions = mapper.convertValue(abi, ABI_FUNCTION_LIST);
        return abiFunctions.stream()
                .filter(f -> "function".equals(f.getType()))
                .collect(Collectors.toList());
    }

    private Optional<AbiFunction> findFunctionByName(@NonNull UUID id, @NonNull String name) {
        return findFunctions(id)
                .stream()
                .filter(f -> name.equals(f.getName()))
                .findFirst();
    }

    private String findTypeForName(@NonNull AbiFunction function, @NonNull String name) {
        return function.getInputs()
                .stream()
                .filter(io -> name.equals(io.getName()))
                .map(AbiFunction.InOuts::getType)
                .findFirst()
                .orElseThrow();
    }

    @Data
    @NoArgsConstructor
    public static final class AbiFunction {
        private String name;
        private String type;

        private List<InOuts> inputs = List.of();
        private List<InOuts> outputs = List.of();

        @Data
        @NoArgsConstructor
        public static class InOuts {
            private String name;
            private String type;
        }
    }
}
