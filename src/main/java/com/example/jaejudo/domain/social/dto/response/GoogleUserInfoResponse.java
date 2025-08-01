package com.example.jaejudo.domain.social.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GoogleUserInfoResponse {

    @JsonProperty("sub")
    private String id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("verified_email")
    private boolean verifiedEmail;

    @JsonProperty("name")
    private String name;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;

    @JsonProperty("picture")
    private String picture;

    @JsonProperty("locale")
    private String locale;
}
