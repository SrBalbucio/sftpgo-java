package balbucio.org.sftpgo.client;

import balbucio.org.sftpgo.model.ApiResponse;
import balbucio.org.sftpgo.model.User;
import balbucio.org.sftpgo.support.QueueHttpClient;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.net.http.HttpRequest;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiClientTest {

    @Test
    void requestBuilderShouldUseBearerAuthorizationHeader() {
        ApiClient client = new ApiClient("http://localhost:8080/api/v2/", () -> "Bearer token-value");

        HttpRequest request = client.requestBuilder("users", "GET").GET().build();

        assertEquals("http://localhost:8080/api/v2/users", request.uri().toString());
        assertEquals("application/json", request.headers().firstValue("Content-Type").orElseThrow());
        assertEquals("Bearer token-value", request.headers().firstValue("Authorization").orElseThrow());
    }

    @Test
    void requestBuilderShouldUseApiKeyHeader() {
        ApiClient client = new ApiClient("http://localhost:8080/api/v2", () -> "api-key-value");

        HttpRequest request = client.requestBuilder("/users", "GET").GET().build();

        assertEquals("api-key-value", request.headers().firstValue("X-SFTPGO-API-KEY").orElseThrow());
    }

    @Test
    void postShouldSerializeRequestAndDeserializeResponse() {
        QueueHttpClient httpClient = new QueueHttpClient()
                .enqueueResponse(200, "{\"username\":\"joao\",\"unknown\":true}");
        ApiClient client = new ApiClient("http://localhost:8080/api/v2", () -> "Bearer jwt", httpClient);
        User payload = User.builder().username("joao").homeDir("/home/joao").build();

        User created = client.post("/users", payload, User.class);
        HttpRequest request = httpClient.getLastRequest();

        assertEquals("POST", request.method());
        assertEquals("http://localhost:8080/api/v2/users", request.uri().toString());
        assertEquals("Bearer jwt", request.headers().firstValue("Authorization").orElseThrow());
        assertTrue(readBody(request).contains("\"username\":\"joao\""));
        assertTrue(readBody(request).contains("\"home_dir\":\"/home/joao\""));
        assertEquals("joao", created.getUsername());
    }

    @Test
    void deleteShouldReturnDefaultOkWhenBodyIsEmpty() {
        QueueHttpClient httpClient = new QueueHttpClient().enqueueResponse(204, "");
        ApiClient client = new ApiClient("http://localhost:8080/api/v2", () -> "Bearer jwt", httpClient);

        ApiResponse response = client.delete("/users/joao");

        assertEquals("OK", response.getMessage());
    }

    @Test
    void getShouldThrowApiExceptionForErrorStatus() {
        QueueHttpClient httpClient = new QueueHttpClient().enqueueResponse(404, "{\"message\":\"missing\"}");
        ApiClient client = new ApiClient("http://localhost:8080/api/v2", () -> "Bearer jwt", httpClient);

        ApiException exception = assertThrows(ApiException.class, () -> client.get("/users/missing", User.class));

        assertEquals(404, exception.getStatusCode());
        assertEquals("{\"message\":\"missing\"}", exception.getResponseBody());
    }

    @Test
    void utilityMethodsShouldBuildExpectedValues() {
        String query = ApiClient.queryString(java.util.Map.of("offset", 0, "limit", 50));
        String basicAuth = ApiClient.basicAuth("admin", "secret");

        assertTrue(query.startsWith("?"));
        assertTrue(query.contains("offset=0"));
        assertTrue(query.contains("limit=50"));
        assertEquals("Basic YWRtaW46c2VjcmV0", basicAuth);
    }

    private static String readBody(HttpRequest request) {
        HttpRequest.BodyPublisher publisher = request.bodyPublisher().orElseThrow();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CompletableFuture<Void> finished = new CompletableFuture<>();

        publisher.subscribe(new Flow.Subscriber<>() {
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(ByteBuffer item) {
                byte[] bytes = new byte[item.remaining()];
                item.get(bytes);
                outputStream.write(bytes, 0, bytes.length);
            }

            @Override
            public void onError(Throwable throwable) {
                finished.completeExceptionally(throwable);
            }

            @Override
            public void onComplete() {
                finished.complete(null);
            }
        });

        finished.join();
        return outputStream.toString(StandardCharsets.UTF_8);
    }
}

