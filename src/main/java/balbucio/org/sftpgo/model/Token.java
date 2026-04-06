package balbucio.org.sftpgo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Token response from GET /token (admin) or GET /user/token (user).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expiration")
    private Long expiration;

    @JsonProperty("expires_at")
    private Long expiresAt;
}
