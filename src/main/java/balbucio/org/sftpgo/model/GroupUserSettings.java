package balbucio.org.sftpgo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Group-level user settings (inherited by users in the group).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupUserSettings {

    @JsonProperty("home_dir")
    private String homeDir;

    @JsonProperty("max_sessions")
    private Integer maxSessions;

    @JsonProperty("quota_size")
    private Long quotaSize;

    @JsonProperty("quota_files")
    private Integer quotaFiles;

    @JsonProperty("permissions")
    private Map<String, List<String>> permissions;

    @JsonProperty("upload_bandwidth")
    private Integer uploadBandwidth;

    @JsonProperty("download_bandwidth")
    private Integer downloadBandwidth;

    @JsonProperty("upload_data_transfer")
    private Integer uploadDataTransfer;

    @JsonProperty("download_data_transfer")
    private Integer downloadDataTransfer;

    @JsonProperty("total_data_transfer")
    private Integer totalDataTransfer;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    @JsonProperty("filters")
    private Object filters;

    @JsonProperty("filesystem")
    private FilesystemConfig filesystem;
}
