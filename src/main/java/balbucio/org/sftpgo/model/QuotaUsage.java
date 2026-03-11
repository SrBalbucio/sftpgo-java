package balbucio.org.sftpgo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Disk quota usage (files and size).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotaUsage {

    @JsonProperty("used_quota_size")
    private Long usedQuotaSize;

    @JsonProperty("used_quota_files")
    private Integer usedQuotaFiles;
}
