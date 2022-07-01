package com.etschel.ethr.abidemo.controller.api;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvokeABIFunctionRequest {

    private String to;

    @Singular
    private List<MethodArguments> params = List.of();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MethodArguments {
        // TODO sort
        private Integer number;
        private String name;
        private String value;
    }
}
