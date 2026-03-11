package balbucio.org.sftpgo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Active user quota scan info.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotaScan {

    @JsonProperty("username")
    private String username;

    @JsonProperty("start_time")
    private Long startTime;
}
