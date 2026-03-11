package balbucio.org.sftpgo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * SFTPGo user (admin-managed account).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("description")
    private String description;

    @JsonProperty("expiration_date")
    private Long expirationDate;

    @JsonProperty("password")
    private String password;

    @JsonProperty("public_keys")
    private List<String> publicKeys;

    @JsonProperty("has_password")
    private Boolean hasPassword;

    @JsonProperty("home_dir")
    private String homeDir;

    @JsonProperty("virtual_folders")
    private List<VirtualFolder> virtualFolders;

    @JsonProperty("uid")
    private Integer uid;

    @JsonProperty("gid")
    private Integer gid;

    @JsonProperty("max_sessions")
    private Integer maxSessions;

    @JsonProperty("quota_size")
    private Long quotaSize;

    @JsonProperty("quota_files")
    private Integer quotaFiles;

    /**
     * Map of path -> list of permission strings (e.g. "/" -> ["*"], "/somedir" -> ["list", "download"]).
     */
    @JsonProperty("permissions")
    private Map<String, List<String>> permissions;

    @JsonProperty("used_quota_size")
    private Long usedQuotaSize;

    @JsonProperty("used_quota_files")
    private Integer usedQuotaFiles;

    @JsonProperty("last_quota_update")
    private Long lastQuotaUpdate;

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

    @JsonProperty("used_upload_data_transfer")
    private Long usedUploadDataTransfer;

    @JsonProperty("used_download_data_transfer")
    private Long usedDownloadDataTransfer;

    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("updated_at")
    private Long updatedAt;

    @JsonProperty("last_login")
    private Long lastLogin;

    @JsonProperty("first_download")
    private Long firstDownload;

    @JsonProperty("first_upload")
    private Long firstUpload;

    @JsonProperty("last_password_change")
    private Long lastPasswordChange;

    @JsonProperty("filters")
    private Object filters;

    @JsonProperty("filesystem")
    private Map<String, Object> filesystem;

    @JsonProperty("additional_info")
    private String additionalInfo;

    @JsonProperty("groups")
    private List<GroupMapping> groups;

    @JsonProperty("role")
    private String role;
}
