package balbucio.org.sftpgo.auth;

import balbucio.org.sftpgo.support.QueueHttpClient;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenProviderTest {

    @Test
    void apiKeyModeShouldReturnConfiguredKey() {
        TokenProvider provider = TokenProvider.apiKey("my-api-key");

        assertEquals("my-api-key", provider.get());
    }

    @Test
    void jwtModeShouldFetchTokenAndCacheBearerHeader() throws Exception {
        QueueHttpClient httpClient = new QueueHttpClient()
                .enqueueResponse(200, "{" +
                        "\"access_token\":\"jwt-token\"," +
                        "\"expiration\":3600," +
                        "\"expires_at\":\"2026-04-06T15:30:00\"," +
                        "\"unexpected\":true" +
                        "}");
        TokenProvider provider = jwtProvider(httpClient);

        String firstHeader = provider.get();
        String secondHeader = provider.get();

        assertEquals("Bearer jwt-token", firstHeader);
        assertEquals("Bearer jwt-token", secondHeader);
        assertEquals(1, httpClient.getSendCount());
        assertEquals("http://localhost:8080/api/v2/token", httpClient.getLastRequest().uri().toString());
        assertEquals("Basic YWRtaW46c2VjcmV0", httpClient.getLastRequest().headers().firstValue(TokenProvider.HEADER_AUTHORIZATION).orElseThrow());
        assertEquals("jwt-token", provider.getBearerToken());
        assertEquals(LocalDateTime.of(2026, 4, 6, 15, 30).toString(), provider.getTokenExpiration());
    }

    @Test
    void refreshTokenShouldWrapNon200Responses() throws Exception {
        QueueHttpClient httpClient = new QueueHttpClient().enqueueResponse(401, "unauthorized");
        TokenProvider provider = jwtProvider(httpClient);

        RuntimeException exception = assertThrows(RuntimeException.class, provider::refreshToken);

        assertTrue(exception.getMessage().contains("Failed to obtain token"));
        assertTrue(exception.getCause().getMessage().contains("Token request failed: 401 unauthorized"));
    }

    @Test
    void refreshTokenShouldWrapInvalidJson() throws Exception {
        QueueHttpClient httpClient = new QueueHttpClient().enqueueResponse(200, "not-json");
        TokenProvider provider = jwtProvider(httpClient);

        RuntimeException exception = assertThrows(RuntimeException.class, provider::refreshToken);

        assertTrue(exception.getMessage().contains("Failed to obtain token"));
        assertTrue(exception.getCause().getMessage().contains("Failed to parse token response"));
    }

    private static TokenProvider jwtProvider(QueueHttpClient httpClient) throws Exception {
        Constructor<TokenProvider> constructor = TokenProvider.class.getDeclaredConstructor(
                String.class,
                String.class,
                String.class,
                String.class,
                java.net.http.HttpClient.class
        );
        constructor.setAccessible(true);
        return constructor.newInstance("http://localhost:8080/api/v2/", "admin", "secret", null, httpClient);
    }
}

