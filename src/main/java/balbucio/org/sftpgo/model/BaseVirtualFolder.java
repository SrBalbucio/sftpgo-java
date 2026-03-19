package balbucio.org.sftpgo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Virtual folder (base schema). Can be extended with virtual_path and quota for per-user mapping.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseVirtualFolder {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("mapped_path")
    private String mappedPath;

    @JsonProperty("description")
    private String description;

    @JsonProperty("used_quota_size")
    private Long usedQuotaSize;

    @JsonProperty("used_quota_files")
    private Integer usedQuotaFiles;

    @JsonProperty("last_quota_update")
    private Long lastQuotaUpdate;

    @JsonProperty("users")
    private List<String> users;

    @JsonProperty("filesystem")
    private FilesystemConfig filesystem;
}
