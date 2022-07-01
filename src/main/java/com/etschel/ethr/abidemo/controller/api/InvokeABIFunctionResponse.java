package com.etschel.ethr.abidemo.controller.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.ResponseBody;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ResponseBody
public class InvokeABIFunctionResponse {
    private String rawTrx;
}
