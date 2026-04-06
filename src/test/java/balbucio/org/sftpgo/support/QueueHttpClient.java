package balbucio.org.sftpgo.support;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import java.io.IOException;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class QueueHttpClient extends HttpClient {

    private final Deque<Object> queuedResults = new ArrayDeque<>();

    private HttpRequest lastRequest;
    private int sendCount;

    public QueueHttpClient enqueueResponse(int statusCode, String body) {
        queuedResults.addLast(new ResponsePlan(statusCode, body));
        return this;
    }

    public QueueHttpClient enqueueFailure(Exception exception) {
        queuedResults.addLast(exception);
        return this;
    }

    public HttpRequest getLastRequest() {
        return lastRequest;
    }

    public int getSendCount() {
        return sendCount;
    }

    @Override
    public Optional<CookieHandler> cookieHandler() {
        return Optional.empty();
    }

    @Override
    public Optional<Duration> connectTimeout() {
        return Optional.empty();
    }

    @Override
    public Redirect followRedirects() {
        return Redirect.NEVER;
    }

    @Override
    public Optional<ProxySelector> proxy() {
        return Optional.empty();
    }

    @Override
    public SSLContext sslContext() {
        return null;
    }

    @Override
    public SSLParameters sslParameters() {
        return new SSLParameters();
    }

    @Override
    public Optional<Authenticator> authenticator() {
        return Optional.empty();
    }

    @Override
    public Version version() {
        return Version.HTTP_1_1;
    }

    @Override
    public Optional<Executor> executor() {
        return Optional.empty();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler) throws IOException, InterruptedException {
        lastRequest = request;
        sendCount++;
        if (queuedResults.isEmpty()) {
            throw new IllegalStateException("No queued HTTP response available");
        }

        Object next = queuedResults.removeFirst();
        if (next instanceof IOException) {
            throw (IOException) next;
        }
        if (next instanceof InterruptedException) {
            throw (InterruptedException) next;
        }
        if (next instanceof RuntimeException) {
            throw (RuntimeException) next;
        }
        if (next instanceof Exception) {
            throw new RuntimeException((Exception) next);
        }

        ResponsePlan plan = (ResponsePlan) next;
        return new FakeHttpResponse<>(request, plan.statusCode, (T) plan.body);
    }

    @Override
    public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler) {
        try {
            return CompletableFuture.completedFuture(send(request, responseBodyHandler));
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request,
                                                            HttpResponse.BodyHandler<T> responseBodyHandler,
                                                            HttpResponse.PushPromiseHandler<T> pushPromiseHandler) {
        return sendAsync(request, responseBodyHandler);
    }

    private static class ResponsePlan {
        private final int statusCode;
        private final String body;

        private ResponsePlan(int statusCode, String body) {
            this.statusCode = statusCode;
            this.body = body;
        }
    }
}

