package balbucio.org.sftpgo.client;

import balbucio.org.sftpgo.model.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Low-level HTTP client for SFTPGo REST API.
 * Handles base URL, auth headers (Bearer or API Key), JSON serialization, and error handling.
 */
@Getter
public class ApiClient {

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_API_KEY = "X-SFTPGO-API-KEY";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";

    private final String baseUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Supplier<String> authHeaderSupplier;

    public ApiClient(String baseUrl, Supplier<String> authHeaderSupplier) {
        this(baseUrl, authHeaderSupplier, HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build());
    }

    public ApiClient(String baseUrl, Supplier<String> authHeaderSupplier, HttpClient httpClient) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.authHeaderSupplier = authHeaderSupplier;
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    /**
     * Build a request with common headers (Content-Type, auth).
     */
    public HttpRequest.Builder requestBuilder(String path, String method) {
        String url = path.startsWith("http") ? path : baseUrl + (path.startsWith("/") ? path : "/" + path);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .header(HEADER_CONTENT_TYPE, APPLICATION_JSON);

        String auth = authHeaderSupplier.get();
        if (auth != null && !auth.isEmpty()) {
            if (auth.startsWith("Bearer ") || auth.startsWith("Basic ")) {
                builder.header(HEADER_AUTHORIZATION, auth);
            } else {
                builder.header(HEADER_API_KEY, auth);
            }
        }
        return builder;
    }

    public <T> T get(String path, Class<T> responseType) {
        return execute(requestBuilder(path, "GET").GET().build(), responseType);
    }

    public <T> T get(String path, TypeReference<T> typeRef) {
        return execute(requestBuilder(path, "GET").GET().build(), typeRef);
    }

    public <T> T post(String path, Object body, Class<T> responseType) {
        return execute(requestBuilder(path, "POST").POST(bodyOf(body)).build(), responseType);
    }

    public <T> T put(String path, Object body, Class<T> responseType) {
        return execute(requestBuilder(path, "PUT").PUT(bodyOf(body)).build(), responseType);
    }

    public ApiResponse delete(String path) {
        HttpResponse<String> response = send(requestBuilder(path, "DELETE").DELETE().build());
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            ApiResponse parsed = parseBody(response.body(), ApiResponse.class);
            return parsed != null ? parsed : ApiResponse.builder().message("OK").build();
        }
        throw new ApiException(response.statusCode(), "Delete failed", response.body());
    }

    public HttpResponse<String> send(HttpRequest request) {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Request failed", e);
        }
    }

    public <T> T execute(HttpRequest request, Class<T> responseType) {
        HttpResponse<String> response = send(request);
        String body = response.body();
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            if (responseType == Void.class || responseType == void.class) {
                return null;
            }
            return parseBody(body, responseType);
        }
        throw new ApiException(response.statusCode(), "API error: " + response.statusCode(), body);
    }

    public <T> T execute(HttpRequest request, TypeReference<T> typeRef) {
        HttpResponse<String> response = send(request);
        String body = response.body();
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return parseBody(body, typeRef);
        }
        throw new ApiException(response.statusCode(), "API error: " + response.statusCode(), body);
    }

    private HttpRequest.BodyPublisher bodyOf(Object body) {
        if (body == null) {
            return HttpRequest.BodyPublishers.noBody();
        }
        try {
            return HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(body), StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Serialization failed", e);
        }
    }

    private <T> T parseBody(String body, Class<T> type) {
        if (body == null || body.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(body, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Deserialization failed: " + body, e);
        }
    }

    private <T> T parseBody(String body, TypeReference<T> typeRef) {
        if (body == null || body.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(body, typeRef);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Deserialization failed: " + body, e);
        }
    }

    /**
     * Build query string from map (e.g. offset=0, limit=100).
     */
    public static String queryString(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> e : params.entrySet()) {
            if (e.getValue() == null) continue;
            if (sb.length() > 0) sb.append("&");
            sb.append(e.getKey()).append("=").append(e.getValue());
        }
        return sb.length() > 0 ? "?" + sb : "";
    }

    /**
     * Create Basic Auth header value for token endpoint.
     */
    public static String basicAuth(String username, String password) {
        String credentials = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }
}
