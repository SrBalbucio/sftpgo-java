package balbucio.org.sftpgo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic API response with a message (e.g. "User updated", "Folder deleted").
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    @JsonProperty("message")
    private String message;
}
