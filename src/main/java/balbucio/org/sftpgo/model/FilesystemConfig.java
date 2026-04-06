package balbucio.org.sftpgo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Storage filesystem configuration. Exactly one of osconfig, s3config, gcsconfig, azblobconfig,
 * cryptconfig, sftpconfig, httpconfig should be set according to provider (0=OS, 1=S3, 2=GCS, 3=Azure, 4=Crypt, 5=SFTP, 6=HTTP).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilesystemConfig {

    /** See {@link FsProvider} (0=Local, 1=S3, 2=GCS, 3=Azure Blob, 4=Crypt, 5=SFTP, 6=HTTP) */
    @JsonProperty("provider")
    private Integer provider;

    @JsonProperty("osconfig")
    private OSFsConfig osconfig;

    @JsonProperty("s3config")
    private S3Config s3config;

    @JsonProperty("gcsconfig")
    private GCSConfig gcsconfig;

    @JsonProperty("azblobconfig")
    private AzureBlobFsConfig azblobconfig;

    @JsonProperty("cryptconfig")
    private CryptFsConfig cryptconfig;

    @JsonProperty("sftpconfig")
    private SFTPFsConfig sftpconfig;

    @JsonProperty("httpconfig")
    private HTTPFsConfig httpconfig;
}
