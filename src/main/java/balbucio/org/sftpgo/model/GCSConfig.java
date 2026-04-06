package balbucio.org.sftpgo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Google Cloud Storage configuration.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GCSConfig {

    @JsonProperty("bucket")
    private String bucket;

    @JsonProperty("credentials")
    private Secret credentials;

    /** 0 = explicit credentials, 1 = Application Default Credentials (ADC) */
    @JsonProperty("automatic_credentials")
    private Integer automaticCredentials;

    @JsonProperty("storage_class")
    private String storageClass;

    @JsonProperty("acl")
    private String acl;

    @JsonProperty("key_prefix")
    private String keyPrefix;

    @JsonProperty("upload_part_size")
    private Integer uploadPartSize;

    @JsonProperty("upload_part_max_time")
    private Integer uploadPartMaxTime;
}
