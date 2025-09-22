package com.example.jaejudo.domain.apikey.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerateKeyRequest {

    private String name;
    private long validDate;
}
