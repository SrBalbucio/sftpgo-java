package balbucio.org.sftpgo.auth;

import balbucio.org.sftpgo.client.ApiClient;
import balbucio.org.sftpgo.model.Token;
import lombok.Getter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.function.Supplier;

/**
 * Provides authentication header for the ApiClient.
 * Supports JWT (via GET /token with Basic Auth) or static API Key.
 */
@Getter
public class TokenProvider implements Supplier<String> {

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_API_KEY = "X-SFTPGO-API-KEY";

    private final String baseUrl;
    private final HttpClient httpClient;
    private final String adminUsername;
    private final String adminPassword;
    private final String apiKey;

    private volatile String bearerToken;
    private volatile String tokenExpiration;

    /**
     * Create a provider that uses JWT. Token is fetched via GET /token with Basic Auth.
     */
    public static TokenProvider jwt(String baseUrl, String adminUsername, String adminPassword) {
        return new TokenProvider(baseUrl, adminUsername, adminPassword, null,
                HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build());
    }

    /**
     * Create a provider that uses a static API key. The key is sent as-is in X-SFTPGO-API-KEY header.
     */
    public static TokenProvider apiKey(String apiKey) {
        return new TokenProvider(null, null, null, apiKey,
                HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build());
    }

    private TokenProvider(String baseUrl, String adminUsername, String adminPassword, String apiKey, HttpClient httpClient) {
        this.baseUrl = baseUrl != null && baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.apiKey = apiKey;
        this.httpClient = httpClient;
    }

    @Override
    public String get() {
        if (apiKey != null && !apiKey.isEmpty()) {
            return apiKey;
        }
        if (bearerToken != null && !bearerToken.isEmpty()) {
            return "Bearer " + bearerToken;
        }
        refreshToken();
        return bearerToken != null ? "Bearer " + bearerToken : null;
    }

    /**
     * Force refresh of JWT by calling GET /token again.
     */
    public void refreshToken() {
        if (apiKey != null || baseUrl == null || adminUsername == null || adminPassword == null) {
            return;
        }
        String url = baseUrl + "/token";
        String credentials = adminUsername + ":" + adminPassword;
        String basic = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .header(HEADER_AUTHORIZATION, basic)
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() != 200) {
                throw new RuntimeException("Token request failed: " + response.statusCode() + " " + response.body());
            }
            Token token = parseToken(response.body());
            if (token != null && token.getAccessToken() != null) {
                this.bearerToken = token.getAccessToken();
                this.tokenExpiration = token.getExpiration();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to obtain token", e);
        }
    }

    private static Token parseToken(String json) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().readValue(json, Token.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse token response", e);
        }
    }
}
