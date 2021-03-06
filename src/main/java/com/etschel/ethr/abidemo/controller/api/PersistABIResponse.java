package com.etschel.ethr.abidemo.controller.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ResponseBody
public class PersistABIResponse {
    private UUID id;

    public static PersistABIResponse from(@NonNull UUID id) {
        return new PersistABIResponse(id);
    }
}
