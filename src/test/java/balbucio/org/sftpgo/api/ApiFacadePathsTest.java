package balbucio.org.sftpgo.api;

import balbucio.org.sftpgo.model.ApiResponse;
import balbucio.org.sftpgo.model.BaseVirtualFolder;
import balbucio.org.sftpgo.model.Group;
import balbucio.org.sftpgo.model.QuotaUsage;
import balbucio.org.sftpgo.model.ResetPasswordRequest;
import balbucio.org.sftpgo.model.TransferQuotaUsage;
import balbucio.org.sftpgo.model.User;
import balbucio.org.sftpgo.support.RecordingApiClient;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiFacadePathsTest {

    @Test
    void usersApiShouldBuildExpectedPathsAndBodies() {
        RecordingApiClient client = new RecordingApiClient();
        UsersApi usersApi = new UsersApi(client);
        User user = User.builder().username("john").build();
        ResetPasswordRequest resetPasswordRequest = ResetPasswordRequest.builder().code("123").password("new-secret").build();

        client.setNextResponse(List.of(user));
        List<User> users = usersApi.getUsers(10, 50, "ASC");
        assertEquals("GET", client.getLastMethod());
        assertTrue(client.getLastPath().startsWith("/users?"));
        assertTrue(client.getLastPath().contains("offset=10"));
        assertTrue(client.getLastPath().contains("limit=50"));
        assertTrue(client.getLastPath().contains("order=ASC"));
        assertEquals(1, users.size());

        client.setNextResponse(ApiResponse.builder().message("updated").build());
        ApiResponse updated = usersApi.updateUser("john doe", user, 1);
        assertEquals("PUT", client.getLastMethod());
        assertEquals("/users/john%20doe?disconnect=1", client.getLastPath());
        assertSame(user, client.getLastBody());
        assertEquals("updated", updated.getMessage());

        client.setNextResponse(ApiResponse.builder().message("sent").build());
        usersApi.forgotPassword("john doe");
        assertEquals("POST", client.getLastMethod());
        assertEquals("/users/john%20doe/forgot-password", client.getLastPath());

        client.setNextResponse(ApiResponse.builder().message("reset").build());
        usersApi.resetPassword("john doe", resetPasswordRequest);
        assertEquals("/users/john%20doe/reset-password", client.getLastPath());
        assertSame(resetPasswordRequest, client.getLastBody());
    }

    @Test
    void groupsAndFoldersApisShouldEncodeNamesAndQueryFlags() {
        RecordingApiClient client = new RecordingApiClient();
        GroupsApi groupsApi = new GroupsApi(client);
        FoldersApi foldersApi = new FoldersApi(client);
        Group group = Group.builder().name("dev team").build();
        BaseVirtualFolder folder = BaseVirtualFolder.builder().name("shared team").build();

        client.setNextResponse(group);
        groupsApi.getGroupByName("dev team", 1);
        assertEquals("GET", client.getLastMethod());
        assertEquals("/groups/dev%20team?confidential_data=1", client.getLastPath());

        client.setNextResponse(ApiResponse.builder().message("deleted").build());
        groupsApi.deleteGroup("dev team");
        assertEquals("DELETE", client.getLastMethod());
        assertEquals("/groups/dev%20team", client.getLastPath());

        client.setNextResponse(folder);
        foldersApi.addFolder(folder, 1);
        assertEquals("POST", client.getLastMethod());
        assertEquals("/folders?confidential_data=1", client.getLastPath());
        assertSame(folder, client.getLastBody());

        client.setNextResponse(ApiResponse.builder().message("updated").build());
        foldersApi.updateFolder("shared team", folder);
        assertEquals("PUT", client.getLastMethod());
        assertEquals("/folders/shared%20team", client.getLastPath());
    }

    @Test
    void quotaApiShouldBuildExpectedQuotaEndpoints() {
        RecordingApiClient client = new RecordingApiClient();
        QuotaApi quotaApi = new QuotaApi(client);
        QuotaUsage quotaUsage = QuotaUsage.builder().usedQuotaSize(1024L).usedQuotaFiles(10).build();
        TransferQuotaUsage transferUsage = TransferQuotaUsage.builder().usedUploadDataTransfer(100L).usedDownloadDataTransfer(200L).build();

        client.setNextResponse(ApiResponse.builder().message("scan started").build());
        quotaApi.startUserQuotaScan("john doe");
        assertEquals("POST", client.getLastMethod());
        assertEquals("/quotas/users/john%20doe/scan", client.getLastPath());

        client.setNextResponse(ApiResponse.builder().message("quota updated").build());
        quotaApi.updateUserQuotaUsage("john doe", quotaUsage, "reset");
        assertEquals("PUT", client.getLastMethod());
        assertEquals("/quotas/users/john%20doe/usage?mode=reset", client.getLastPath());
        assertSame(quotaUsage, client.getLastBody());

        client.setNextResponse(ApiResponse.builder().message("transfer updated").build());
        quotaApi.updateUserTransferQuotaUsage("john doe", transferUsage, "add");
        assertEquals("/quotas/users/john%20doe/transfer-usage?mode=add", client.getLastPath());
        assertSame(transferUsage, client.getLastBody());

        client.setNextResponse(ApiResponse.builder().message("folder updated").build());
        quotaApi.updateFolderQuotaUsage("shared team", quotaUsage);
        assertEquals("/quotas/folders/shared%20team/usage", client.getLastPath());
    }
}

