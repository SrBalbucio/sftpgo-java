package balbucio.org.sftpgo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Transfer quota usage (upload/download in bytes).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferQuotaUsage {

    @JsonProperty("used_upload_data_transfer")
    private Long usedUploadDataTransfer;

    @JsonProperty("used_download_data_transfer")
    private Long usedDownloadDataTransfer;
}
