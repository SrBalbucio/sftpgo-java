package balbucio.org.sftpgo.api;

import balbucio.org.sftpgo.client.ApiClient;
import balbucio.org.sftpgo.model.ApiResponse;
import balbucio.org.sftpgo.model.Group;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API for managing groups (SFTPGo admin API).
 */
public class GroupsApi {

    private static final String PATH = "/groups";

    private final ApiClient client;

    public GroupsApi(ApiClient client) {
        this.client = client;
    }

    /**
     * Get groups with optional pagination and ordering.
     */
    public List<Group> getGroups(Integer offset, Integer limit, String order) {
        Map<String, Object> params = new HashMap<>();
        if (offset != null) params.put("offset", offset);
        if (limit != null) params.put("limit", limit);
        if (order != null) params.put("order", order);
        String path = params.isEmpty() ? PATH : PATH + ApiClient.queryString(params);
        return client.get(path, new com.fasterxml.jackson.core.type.TypeReference<List<Group>>() {});
    }

    public List<Group> getGroups() {
        return client.get(PATH, new com.fasterxml.jackson.core.type.TypeReference<List<Group>>() {});
    }

    /**
     * Add a new group.
     */
    public Group addGroup(Group group, Integer confidentialData) {
        String path = PATH;
        if (confidentialData != null && confidentialData == 1) {
            path = PATH + "?confidential_data=1";
        }
        return client.post(path, group, Group.class);
    }

    public Group addGroup(Group group) {
        return client.post(PATH, group, Group.class);
    }

    /**
     * Get group by name.
     */
    public Group getGroupByName(String name, Integer confidentialData) {
        String path = "/groups/" + encodePath(name);
        if (confidentialData != null && confidentialData == 1) {
            path += "?confidential_data=1";
        }
        return client.get(path, Group.class);
    }

    public Group getGroupByName(String name) {
        return client.get("/groups/" + encodePath(name), Group.class);
    }

    /**
     * Update an existing group.
     */
    public ApiResponse updateGroup(String name, Group group) {
        return client.put("/groups/" + encodePath(name), group, ApiResponse.class);
    }

    /**
     * Delete a group.
     */
    public ApiResponse deleteGroup(String name) {
        return client.delete("/groups/" + encodePath(name));
    }

    private static String encodePath(String segment) {
        return java.net.URLEncoder.encode(segment, java.nio.charset.StandardCharsets.UTF_8).replace("+", "%20");
    }
}
