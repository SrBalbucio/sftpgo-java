package balbucio.org.sftpgo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

/**
 * Virtual folder mapping for a user: path + quota. Used inside User and Group.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualFolder extends BaseVirtualFolder {

    @JsonProperty("virtual_path")
    private String virtualPath;

    @JsonProperty("quota_size")
    private Long quotaSize;

    @JsonProperty("quota_files")
    private Integer quotaFiles;
}
