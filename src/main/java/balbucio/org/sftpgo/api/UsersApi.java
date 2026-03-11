package balbucio.org.sftpgo.api;

import balbucio.org.sftpgo.client.ApiClient;
import balbucio.org.sftpgo.model.ApiResponse;
import balbucio.org.sftpgo.model.ResetPasswordRequest;
import balbucio.org.sftpgo.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API for managing users (SFTPGo admin API).
 */
public class UsersApi {

    private static final String PATH = "/users";

    private final ApiClient client;

    public UsersApi(ApiClient client) {
        this.client = client;
    }

    /**
     * Get users with optional pagination and ordering.
     */
    public List<User> getUsers(Integer offset, Integer limit, String order) {
        Map<String, Object> params = new HashMap<>();
        if (offset != null) params.put("offset", offset);
        if (limit != null) params.put("limit", limit);
        if (order != null) params.put("order", order);
        String path = params.isEmpty() ? PATH : PATH + ApiClient.queryString(params);
        return client.get(path, new com.fasterxml.jackson.core.type.TypeReference<List<User>>() {});
    }

    public List<User> getUsers() {
        return client.get(PATH, new com.fasterxml.jackson.core.type.TypeReference<List<User>>() {});
    }

    /**
     * Add a new user.
     */
    public User addUser(User user, Integer confidentialData) {
        String path = PATH;
        if (confidentialData != null && confidentialData == 1) {
            path = PATH + "?confidential_data=1";
        }
        return client.post(path, user, User.class);
    }

    public User addUser(User user) {
        return client.post(PATH, user, User.class);
    }

    /**
     * Get user by username.
     */
    public User getUserByUsername(String username, Integer confidentialData) {
        String path = "/users/" + encodePath(username);
        if (confidentialData != null && confidentialData == 1) {
            path += "?confidential_data=1";
        }
        return client.get(path, User.class);
    }

    public User getUserByUsername(String username) {
        return client.get("/users/" + encodePath(username), User.class);
    }

    /**
     * Update an existing user.
     *
     * @param disconnect 1 to disconnect the user after update so new settings apply on next login
     */
    public ApiResponse updateUser(String username, User user, Integer disconnect) {
        String path = "/users/" + encodePath(username);
        if (disconnect != null && disconnect == 1) {
            path += "?disconnect=1";
        }
        return client.put(path, user, ApiResponse.class);
    }

    public ApiResponse updateUser(String username, User user) {
        return client.put("/users/" + encodePath(username), user, ApiResponse.class);
    }

    /**
     * Delete a user.
     */
    public ApiResponse deleteUser(String username) {
        return client.delete("/users/" + encodePath(username));
    }

    /**
     * Disable second factor authentication for the user.
     */
    public ApiResponse disable2FA(String username) {
        return client.put("/users/" + encodePath(username) + "/2fa/disable", null, ApiResponse.class);
    }

    /**
     * Send password reset code by email (no auth required). User must have valid email and reset not disabled.
     */
    public ApiResponse forgotPassword(String username) {
        String path = "/users/" + encodePath(username) + "/forgot-password";
        return client.post(path, null, ApiResponse.class);
    }

    /**
     * Reset password using code received by email (no auth required).
     */
    public ApiResponse resetPassword(String username, ResetPasswordRequest request) {
        return client.post("/users/" + encodePath(username) + "/reset-password", request, ApiResponse.class);
    }

    private static String encodePath(String segment) {
        return java.net.URLEncoder.encode(segment, java.nio.charset.StandardCharsets.UTF_8).replace("+", "%20");
    }
}
