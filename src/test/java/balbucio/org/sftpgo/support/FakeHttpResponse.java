package balbucio.org.sftpgo.support;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class FakeHttpResponse<T> implements HttpResponse<T> {

    private final HttpRequest request;
    private final int statusCode;
    private final T body;

    public FakeHttpResponse(HttpRequest request, int statusCode, T body) {
        this.request = request;
        this.statusCode = statusCode;
        this.body = body;
    }

    @Override
    public int statusCode() {
        return statusCode;
    }

    @Override
    public HttpRequest request() {
        return request;
    }

    @Override
    public Optional<HttpResponse<T>> previousResponse() {
        return Optional.empty();
    }

    @Override
    public HttpHeaders headers() {
        return HttpHeaders.of(java.util.Map.of(), (name, value) -> true);
    }

    @Override
    public T body() {
        return body;
    }

    @Override
    public Optional<SSLSession> sslSession() {
        return Optional.empty();
    }

    @Override
    public URI uri() {
        return request != null ? request.uri() : URI.create("http://localhost");
    }

    @Override
    public HttpClient.Version version() {
        return HttpClient.Version.HTTP_1_1;
    }
}

