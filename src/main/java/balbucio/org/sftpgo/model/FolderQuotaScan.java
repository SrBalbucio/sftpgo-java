package balbucio.org.sftpgo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Active folder quota scan info.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderQuotaScan {

    @JsonProperty("name")
    private String name;

    @JsonProperty("start_time")
    private Long startTime;
}
