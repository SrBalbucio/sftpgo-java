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
 * Group mapping for a user (primary/secondary/membership).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMapping {

    @JsonProperty("name")
    private String name;

    /**
     * 1 = Primary, 2 = Secondary, 3 = Membership only.
     */
    @JsonProperty("type")
    private Integer type;
}
