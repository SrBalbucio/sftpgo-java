package balbucio.org.sftpgo.model;

import lombok.Getter;

/**
 * Filesystem provider codes used in FilesystemConfig.provider.
 *
 * @see <a href="https://github.com/drakkan/sftpgo/blob/main/openapi/openapi.yaml">OpenAPI</a>
 */
@Getter
public enum FsProvider {

    /** Local filesystem */
    LOCAL(0),
    /** S3 Compatible Object Storage */
    S3(1),
    /** Google Cloud Storage */
    GCS(2),
    /** Azure Blob Storage */
    AZURE_BLOB(3),
    /** Local filesystem encrypted */
    CRYPT(4),
    /** SFTP */
    SFTP(5),
    /** HTTP filesystem */
    HTTP(6);

    private final int code;

    FsProvider(int code) {
        this.code = code;
    }

    public static FsProvider fromCode(Integer code) {
        if (code == null) return null;
        for (FsProvider p : values()) {
            if (p.code == code) return p;
        }
        return null;
    }
}
