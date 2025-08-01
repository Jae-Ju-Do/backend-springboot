package com.example.jaejudo.domain.social.service;

import com.example.jaejudo.domain.social.dto.OAuth2MemberInfo;

public interface OAuth2Service {

    String getAuthorizeUrl();
    OAuth2MemberInfo getMemberInfo(String code);
    String getProviderName();
}
