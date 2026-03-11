package balbucio.org.sftpgo.api;

import balbucio.org.sftpgo.client.ApiClient;
import balbucio.org.sftpgo.model.ApiResponse;
import balbucio.org.sftpgo.model.BaseVirtualFolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API for managing virtual folders (SFTPGo admin API).
 */
public class FoldersApi {

    private static final String PATH = "/folders";

    private final ApiClient client;

    public FoldersApi(ApiClient client) {
        this.client = client;
    }

    /**
     * Get folders with optional pagination and ordering.
     *
     * @param offset optional start offset (default 0)
     * @param limit  optional max items (default 100, max 500)
     * @param order  optional "ASC" or "DESC" by name
     * @return list of folders
     */
    public List<BaseVirtualFolder> getFolders(Integer offset, Integer limit, String order) {
        Map<String, Object> params = new HashMap<>();
        if (offset != null) params.put("offset", offset);
        if (limit != null) params.put("limit", limit);
        if (order != null) params.put("order", order);
        String path = params.isEmpty() ? PATH : PATH + ApiClient.queryString(params);
        return client.get(path, new com.fasterxml.jackson.core.type.TypeReference<List<BaseVirtualFolder>>() {});
    }

    public List<BaseVirtualFolder> getFolders() {
        return client.get(PATH, new com.fasterxml.jackson.core.type.TypeReference<List<BaseVirtualFolder>>() {});
    }

    /**
     * Add a new folder.
     *
     * @param folder            folder to create
     * @param confidentialData if 1, response may include confidential data
     * @return created folder
     */
    public BaseVirtualFolder addFolder(BaseVirtualFolder folder, Integer confidentialData) {
        String path = PATH;
        if (confidentialData != null && confidentialData == 1) {
            path = PATH + "?confidential_data=1";
        }
        return client.post(path, folder, BaseVirtualFolder.class);
    }

    public BaseVirtualFolder addFolder(BaseVirtualFolder folder) {
        return client.post(PATH, folder, BaseVirtualFolder.class);
    }

    /**
     * Get folder by name.
     */
    public BaseVirtualFolder getFolderByName(String name, Integer confidentialData) {
        String path = "/folders/" + encodePath(name);
        if (confidentialData != null && confidentialData == 1) {
            path += "?confidential_data=1";
        }
        return client.get(path, BaseVirtualFolder.class);
    }

    public BaseVirtualFolder getFolderByName(String name) {
        return client.get("/folders/" + encodePath(name), BaseVirtualFolder.class);
    }

    /**
     * Update an existing folder.
     */
    public ApiResponse updateFolder(String name, BaseVirtualFolder folder) {
        return client.put("/folders/" + encodePath(name), folder, ApiResponse.class);
    }

    /**
     * Delete a folder.
     */
    public ApiResponse deleteFolder(String name) {
        return client.delete("/folders/" + encodePath(name));
    }

    private static String encodePath(String segment) {
        return java.net.URLEncoder.encode(segment, java.nio.charset.StandardCharsets.UTF_8).replace("+", "%20");
    }
}
