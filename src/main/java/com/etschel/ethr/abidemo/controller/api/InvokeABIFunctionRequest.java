package com.etschel.ethr.abidemo.controller.api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class InvokeABIFunctionRequest {

    private String to;
    private List<MethodArguments> params = List.of();

    @Data
    @NoArgsConstructor
    public static class MethodArguments {
        // TODO sort
        private Integer number;
        private String name;
        private String value;
    }
}
