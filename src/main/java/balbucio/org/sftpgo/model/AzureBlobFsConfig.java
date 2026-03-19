package balbucio.org.sftpgo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Azure Blob Storage configuration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AzureBlobFsConfig {

    @JsonProperty("container")
    private String container;

    @JsonProperty("account_name")
    private String accountName;

    @JsonProperty("account_key")
    private Secret accountKey;

    @JsonProperty("sas_url")
    private Secret sasUrl;

    @JsonProperty("endpoint")
    private String endpoint;

    @JsonProperty("upload_part_size")
    private Integer uploadPartSize;

    @JsonProperty("upload_concurrency")
    private Integer uploadConcurrency;

    @JsonProperty("download_part_size")
    private Integer downloadPartSize;

    @JsonProperty("download_concurrency")
    private Integer downloadConcurrency;

    /** Archive, Hot, Cool or empty */
    @JsonProperty("access_tier")
    private String accessTier;

    @JsonProperty("key_prefix")
    private String keyPrefix;

    @JsonProperty("use_emulator")
    private Boolean useEmulator;
}
