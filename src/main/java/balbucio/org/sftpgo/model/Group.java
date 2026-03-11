package balbucio.org.sftpgo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SFTPGo group. Users inherit settings from their groups.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("updated_at")
    private Long updatedAt;

    @JsonProperty("user_settings")
    private GroupUserSettings userSettings;

    @JsonProperty("virtual_folders")
    private List<VirtualFolder> virtualFolders;

    @JsonProperty("users")
    private List<String> users;

    @JsonProperty("admins")
    private List<String> admins;
}
