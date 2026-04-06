package balbucio.org.sftpgo;

import balbucio.org.sftpgo.client.ApiClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class SftpGoClientTest {

    @Test
    void shouldReuseProvidedApiClientAndExposeApiFacades() {
        ApiClient apiClient = new ApiClient("http://localhost:8080/api/v2/", () -> "api-key-value");
        SftpGoClient client = new SftpGoClient(apiClient);

        assertSame(apiClient, client.getApiClient());
        assertNotNull(client.users());
        assertNotNull(client.groups());
        assertNotNull(client.quota());
        assertNotNull(client.folders());
        assertSame(client.getUsersApi(), client.users());
        assertSame(client.getGroupsApi(), client.groups());
        assertSame(client.getQuotaApi(), client.quota());
        assertSame(client.getFoldersApi(), client.folders());
    }

    @Test
    void shouldCreateClientUsingApiKeyConstructor() {
        SftpGoClient client = new SftpGoClient("http://localhost:8080/api/v2/", "api-key-value");

        assertNotNull(client.getApiClient());
        assertNotNull(client.users());
        assertNotNull(client.groups());
        assertNotNull(client.quota());
        assertNotNull(client.folders());
    }
}

