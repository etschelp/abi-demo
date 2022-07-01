package com.etschel.ethr.abidemo.controller.api;

import lombok.*;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Data
@Builder
@ResponseBody
@NoArgsConstructor
@AllArgsConstructor
public class ABIFunctionsResponse {
    private List<String> functionNames;

    public static ABIFunctionsResponse from(@NonNull List<String> functionNames) {
        return ABIFunctionsResponse.builder().functionNames(functionNames).build();
    }
}
