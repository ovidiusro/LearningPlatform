package com.hozan.univer.security;

import java.util.Map;

public interface TokenService {

    String permanent(Map<String, String> attributes);

    String expiring(Map<String, String> attributes);

    Map<String, String> untrusted(String token);

    Map<String, String> verify(String token);

    boolean containToken(Long id);

    void saveToken(Long id, String token);

    boolean deleteToken(Long id);
}
