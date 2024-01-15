package com.example.securityTest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    GUEST("ROLE_QUEST"), USER("ROLE_USER");

    private final String key;
}
