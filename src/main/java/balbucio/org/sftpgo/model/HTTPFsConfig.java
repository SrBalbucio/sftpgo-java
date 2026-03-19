package balbucio.org.sftpgo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * HTTP/S filesystem configuration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HTTPFsConfig {

    @JsonProperty("endpoint")
    private String endpoint;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private Secret password;

    @JsonProperty("api_key")
    private Secret apiKey;

    @JsonProperty("skip_tls_verify")
    private Boolean skipTlsVerify;

    /** 0 = username+endpoint must match, 1 = only endpoint */
    @JsonProperty("equality_check_mode")
    private Integer equalityCheckMode;
}
