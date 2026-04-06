package balbucio.org.sftpgo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Encrypted/sensitive value. Use status "Plain" + payload to set a new secret; "Redacted" to keep existing.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Secret {

    /** Plain, AES-256-GCM, Secretbox, GCP, AWS, VaultTransit, AzureKeyVault, Redacted */
    @JsonProperty("status")
    private String status;

    @JsonProperty("payload")
    private String payload;

    @JsonProperty("key")
    private String key;

    @JsonProperty("additional_data")
    private String additionalData;

    @JsonProperty("mode")
    private Integer mode;
}
