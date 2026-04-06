package balbucio.org.sftpgo.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JacksonModelCompatibilityTest {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void shouldIgnoreUnknownPropertiesAcrossNestedModels() throws Exception {
        String json = "{" +
                "\"username\":\"joao\"," +
                "\"home_dir\":\"/home/joao\"," +
                "\"unexpected_root\":true," +
                "\"filesystem\":{" +
                "  \"provider\":1," +
                "  \"unknown_fs\":\"ignored\"," +
                "  \"s3config\":{" +
                "    \"bucket\":\"demo-bucket\"," +
                "    \"key_prefix\":\"users/joao\"," +
                "    \"unknown_s3\":123" +
                "  }" +
                "}," +
                "\"virtual_folders\":[{" +
                "  \"name\":\"shared\"," +
                "  \"mapped_path\":\"/srv/shared\"," +
                "  \"virtual_path\":\"/shared\"," +
                "  \"quota_size\":0," +
                "  \"unknown_folder\":\"ignored\"" +
                "}]" +
                "}";

        User user = objectMapper.readValue(json, User.class);

        assertEquals("joao", user.getUsername());
        assertEquals("/home/joao", user.getHomeDir());
        assertNotNull(user.getFilesystem());
        assertEquals(1, user.getFilesystem().getProvider());
        assertNotNull(user.getFilesystem().getS3config());
        assertEquals("demo-bucket", user.getFilesystem().getS3config().getBucket());
        assertNotNull(user.getVirtualFolders());
        assertEquals(1, user.getVirtualFolders().size());
        assertEquals("/shared", user.getVirtualFolders().get(0).getVirtualPath());
    }

    @Test
    void shouldDeserializeTokenWithUnknownProperties() throws Exception {
        String json = "{" +
                "\"access_token\":\"jwt-token\"," +
                "\"expiration\":3600," +
                "\"expires_at\":\"2026-04-06T15:30:00\"," +
                "\"unexpected\":\"ignored\"" +
                "}";

        Token token = objectMapper.readValue(json, Token.class);

        assertEquals("jwt-token", token.getAccessToken());
        assertEquals(3600L, token.getExpiration());
        assertEquals(LocalDateTime.of(2026, 4, 6, 15, 30), token.getExpiresAt());
    }
}

