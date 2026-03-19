package balbucio.org.sftpgo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * S3-compatible object storage configuration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class S3Config {

    @JsonProperty("bucket")
    private String bucket;

    @JsonProperty("region")
    private String region;

    @JsonProperty("access_key")
    private String accessKey;

    @JsonProperty("access_secret")
    private Secret accessSecret;

    @JsonProperty("sse_customer_key")
    private Secret sseCustomerKey;

    @JsonProperty("role_arn")
    private String roleArn;

    @JsonProperty("session_token")
    private String sessionToken;

    @JsonProperty("endpoint")
    private String endpoint;

    @JsonProperty("storage_class")
    private String storageClass;

    @JsonProperty("acl")
    private String acl;

    @JsonProperty("upload_part_size")
    private Integer uploadPartSize;

    @JsonProperty("upload_concurrency")
    private Integer uploadConcurrency;

    @JsonProperty("upload_part_max_time")
    private Integer uploadPartMaxTime;

    @JsonProperty("download_part_size")
    private Integer downloadPartSize;

    @JsonProperty("download_concurrency")
    private Integer downloadConcurrency;

    @JsonProperty("download_part_max_time")
    private Integer downloadPartMaxTime;

    @JsonProperty("force_path_style")
    private Boolean forcePathStyle;

    @JsonProperty("key_prefix")
    private String keyPrefix;
}
