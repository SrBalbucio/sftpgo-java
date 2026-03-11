package balbucio.org.sftpgo.client;

import lombok.Getter;

/**
 * Exception thrown when the SFTPGo REST API returns an error response (4xx or 5xx).
 */
@Getter
public class ApiException extends RuntimeException {

    private final int statusCode;
    private final String responseBody;

    public ApiException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = null;
    }

    public ApiException(int statusCode, String message, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }
}
