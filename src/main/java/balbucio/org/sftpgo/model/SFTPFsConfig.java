package balbucio.org.sftpgo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SFTP filesystem configuration (remote SFTP as storage).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SFTPFsConfig {

    @JsonProperty("endpoint")
    private String endpoint;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private Secret password;

    @JsonProperty("private_key")
    private Secret privateKey;

    @JsonProperty("key_passphrase")
    private Secret keyPassphrase;

    @JsonProperty("fingerprints")
    private List<String> fingerprints;

    @JsonProperty("prefix")
    private String prefix;

    @JsonProperty("disable_concurrent_reads")
    private Boolean disableConcurrentReads;

    @JsonProperty("buffer_size")
    private Integer bufferSize;

    /** 0 = username+endpoint must match, 1 = only endpoint */
    @JsonProperty("equality_check_mode")
    private Integer equalityCheckMode;
}
