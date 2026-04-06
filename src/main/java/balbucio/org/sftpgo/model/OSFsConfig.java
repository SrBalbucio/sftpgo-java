package balbucio.org.sftpgo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Local (OS) filesystem configuration. Buffer sizes in MB (0–10).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OSFsConfig {

    @JsonProperty("read_buffer_size")
    private Integer readBufferSize;

    @JsonProperty("write_buffer_size")
    private Integer writeBufferSize;
}
