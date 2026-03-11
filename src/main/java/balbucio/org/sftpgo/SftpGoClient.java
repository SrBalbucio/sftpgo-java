package balbucio.org.sftpgo;

import balbucio.org.sftpgo.api.FoldersApi;
import balbucio.org.sftpgo.api.GroupsApi;
import balbucio.org.sftpgo.api.QuotaApi;
import balbucio.org.sftpgo.api.UsersApi;
import balbucio.org.sftpgo.auth.TokenProvider;
import balbucio.org.sftpgo.client.ApiClient;
import lombok.Getter;

/**
 * Main entry point for the SFTPGo REST API SDK.
 * Configure base URL and authentication (JWT or API Key), then access users(), groups(), quota(), and folders().
 */
@Getter
public class SftpGoClient {

    private final ApiClient apiClient;
    private final UsersApi usersApi;
    private final GroupsApi groupsApi;
    private final QuotaApi quotaApi;
    private final FoldersApi foldersApi;

    /**
     * Create client with JWT authentication. Token is obtained via GET /token with Basic Auth.
     *
     * @param baseUrl        e.g. "http://localhost:8080/api/v2"
     * @param adminUsername  admin username for token
     * @param adminPassword  admin password for token
     */
    public SftpGoClient(String baseUrl, String adminUsername, String adminPassword) {
        TokenProvider tokenProvider = TokenProvider.jwt(baseUrl, adminUsername, adminPassword);
        this.apiClient = new ApiClient(baseUrl, tokenProvider);
        this.usersApi = new UsersApi(apiClient);
        this.groupsApi = new GroupsApi(apiClient);
        this.quotaApi = new QuotaApi(apiClient);
        this.foldersApi = new FoldersApi(apiClient);
    }

    /**
     * Create client with API key authentication (X-SFTPGO-API-KEY header).
     *
     * @param baseUrl e.g. "http://localhost:8080/api/v2"
     * @param apiKey  the API key
     */
    public SftpGoClient(String baseUrl, String apiKey) {
        TokenProvider tokenProvider = TokenProvider.apiKey(apiKey);
        this.apiClient = new ApiClient(baseUrl, tokenProvider);
        this.usersApi = new UsersApi(apiClient);
        this.groupsApi = new GroupsApi(apiClient);
        this.quotaApi = new QuotaApi(apiClient);
        this.foldersApi = new FoldersApi(apiClient);
    }

    /**
     * Create client with a custom ApiClient (e.g. custom HttpClient or auth supplier).
     */
    public SftpGoClient(ApiClient apiClient) {
        this.apiClient = apiClient;
        this.usersApi = new UsersApi(apiClient);
        this.groupsApi = new GroupsApi(apiClient);
        this.quotaApi = new QuotaApi(apiClient);
        this.foldersApi = new FoldersApi(apiClient);
    }

    public UsersApi users() {
        return usersApi;
    }

    public GroupsApi groups() {
        return groupsApi;
    }

    public QuotaApi quota() {
        return quotaApi;
    }

    public FoldersApi folders() {
        return foldersApi;
    }
}
