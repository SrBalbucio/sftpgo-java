package balbucio.org.sftpgo.api;

import balbucio.org.sftpgo.client.ApiClient;
import balbucio.org.sftpgo.model.ApiResponse;
import balbucio.org.sftpgo.model.FolderQuotaScan;
import balbucio.org.sftpgo.model.QuotaScan;
import balbucio.org.sftpgo.model.QuotaUsage;
import balbucio.org.sftpgo.model.TransferQuotaUsage;

import java.util.List;

/**
 * API for quota scans and usage updates (SFTPGo admin API).
 */
public class QuotaApi {

    private static final String QUOTAS_USERS_SCANS = "/quotas/users/scans";
    private static final String QUOTAS_FOLDERS_SCANS = "/quotas/folders/scans";

    private final ApiClient client;

    public QuotaApi(ApiClient client) {
        this.client = client;
    }

    // --- User quota scans ---

    /**
     * Get active user quota scans.
     */
    public List<QuotaScan> getUserQuotaScans() {
        return client.get(QUOTAS_USERS_SCANS, new com.fasterxml.jackson.core.type.TypeReference<List<QuotaScan>>() {});
    }

    /**
     * Start a quota scan for the given user.
     */
    public ApiResponse startUserQuotaScan(String username) {
        return client.post("/quotas/users/" + encodePath(username) + "/scan", null, ApiResponse.class);
    }

    /**
     * Update disk quota usage for a user.
     *
     * @param mode "add" to add to current values, "reset" (default) to replace
     */
    public ApiResponse updateUserQuotaUsage(String username, QuotaUsage usage, String mode) {
        String path = "/quotas/users/" + encodePath(username) + "/usage";
        if (mode != null && !mode.isEmpty()) {
            path += "?mode=" + mode;
        }
        return client.put(path, usage, ApiResponse.class);
    }

    public ApiResponse updateUserQuotaUsage(String username, QuotaUsage usage) {
        return client.put("/quotas/users/" + encodePath(username) + "/usage", usage, ApiResponse.class);
    }

    /**
     * Update transfer quota usage for a user.
     *
     * @param mode "add" or "reset" (default)
     */
    public ApiResponse updateUserTransferQuotaUsage(String username, TransferQuotaUsage usage, String mode) {
        String path = "/quotas/users/" + encodePath(username) + "/transfer-usage";
        if (mode != null && !mode.isEmpty()) {
            path += "?mode=" + mode;
        }
        return client.put(path, usage, ApiResponse.class);
    }

    public ApiResponse updateUserTransferQuotaUsage(String username, TransferQuotaUsage usage) {
        return client.put("/quotas/users/" + encodePath(username) + "/transfer-usage", usage, ApiResponse.class);
    }

    // --- Folder quota scans ---

    /**
     * Get active folder quota scans.
     */
    public List<FolderQuotaScan> getFolderQuotaScans() {
        return client.get(QUOTAS_FOLDERS_SCANS, new com.fasterxml.jackson.core.type.TypeReference<List<FolderQuotaScan>>() {});
    }

    /**
     * Start a quota scan for the given folder.
     */
    public ApiResponse startFolderQuotaScan(String folderName) {
        return client.post("/quotas/folders/" + encodePath(folderName) + "/scan", null, ApiResponse.class);
    }

    /**
     * Update folder quota usage.
     *
     * @param mode "add" or "reset" (default)
     */
    public ApiResponse updateFolderQuotaUsage(String folderName, QuotaUsage usage, String mode) {
        String path = "/quotas/folders/" + encodePath(folderName) + "/usage";
        if (mode != null && !mode.isEmpty()) {
            path += "?mode=" + mode;
        }
        return client.put(path, usage, ApiResponse.class);
    }

    public ApiResponse updateFolderQuotaUsage(String folderName, QuotaUsage usage) {
        return client.put("/quotas/folders/" + encodePath(folderName) + "/usage", usage, ApiResponse.class);
    }

    private static String encodePath(String segment) {
        return java.net.URLEncoder.encode(segment, java.nio.charset.StandardCharsets.UTF_8).replace("+", "%20");
    }
}
