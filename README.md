# sftpgo-java

SDK em Java 11 para a [API REST do SFTPGo](https://sftpgo.com/rest-api). Inclui as categorias **Users**, **Groups**, **Quota** e **Folder** (virtual folders).

[![](https://img.shields.io/badge/HyperPowered-Use%20the%20official%20repository-yellow?color=%23279BF8&cacheSeconds=3600)](https://maven.dev.hyperpowered.net/#/releases/balbucio/org/sftpgo/)


## Requisitos

- Java 11+
- Maven (para build)

## Uso

### Criar o cliente

**Autenticação por JWT** (usuário e senha do admin; o token é obtido automaticamente em `GET /token`):

```java
import balbucio.org.sftpgo.SftpGoClient;

String baseUrl = "http://localhost:8080/api/v2";
SftpGoClient client = new SftpGoClient(baseUrl, "admin", "senha");
```

**Autenticação por API Key** (header `X-SFTPGO-API-KEY`):

```java
SftpGoClient client = new SftpGoClient(baseUrl, "sua-api-key");
```

### Usuários (Users)

```java
import balbucio.org.sftpgo.model.User;
import balbucio.org.sftpgo.model.ResetPasswordRequest;
import java.util.List;
import java.util.Map;

// Listar
List<User> users = client.users().getUsers();
List<User> usersPage = client.users().getUsers(0, 50, "ASC");

// Criar
User user = User.builder()
    .username("joao")
    .password("senha123")
    .homeDir("/home/joao")
    .status(1)
    .permissions(Map.of("/", List.of("*")))
    .build();
User created = client.users().addUser(user);

// Buscar, atualizar, remover
User u = client.users().getUserByUsername("joao");
client.users().updateUser("joao", user);
client.users().deleteUser("joao");

// 2FA e senha
client.users().disable2FA("joao");
client.users().forgotPassword("joao");
client.users().resetPassword("joao", ResetPasswordRequest.builder().code("123456").password("novaSenha").build());
```

### Grupos (Groups)

```java
import balbucio.org.sftpgo.model.Group;
import java.util.List;

List<Group> groups = client.groups().getGroups();
Group g = client.groups().getGroupByName("desenvolvimento");
Group newGroup = Group.builder().name("novo").build();
client.groups().addGroup(newGroup);
client.groups().updateGroup("novo", newGroup);
client.groups().deleteGroup("novo");
```

### Pastas virtuais (Folders)

```java
import balbucio.org.sftpgo.model.BaseVirtualFolder;
import java.util.List;

List<BaseVirtualFolder> folders = client.folders().getFolders();
BaseVirtualFolder folder = BaseVirtualFolder.builder()
    .name("compartilhado")
    .mappedPath("/var/sftpgo/shared")
    .build();
client.folders().addFolder(folder);
client.folders().getFolderByName("compartilhado");
client.folders().updateFolder("compartilhado", folder);
client.folders().deleteFolder("compartilhado");
```

### Configuração de filesystem (S3, GCS, Azure, etc.)

Pastas virtuais e usuários podem usar um backend de armazenamento configurado via `FilesystemConfig`:

```java
import balbucio.org.sftpgo.model.FilesystemConfig;
import balbucio.org.sftpgo.model.S3Config;
import balbucio.org.sftpgo.model.GCSConfig;
import balbucio.org.sftpgo.model.AzureBlobFsConfig;
import balbucio.org.sftpgo.model.FsProvider;
import balbucio.org.sftpgo.model.Secret;

// S3 (provider = 1)
S3Config s3 = S3Config.builder()
    .bucket("meu-bucket")
    .region("us-east-1")
    .accessKey("AKIA...")
    .accessSecret(Secret.builder().status("Plain").payload("secret").build())
    .keyPrefix("pasta/")  // opcional: chroot no bucket
    .build();
FilesystemConfig fs = FilesystemConfig.builder()
    .provider(FsProvider.S3.getCode())
    .s3config(s3)
    .build();

// Google Cloud Storage (provider = 2)
GCSConfig gcs = GCSConfig.builder()
    .bucket("meu-bucket-gcs")
    .credentials(Secret.builder().status("Plain").payload("{\"type\":\"service_account\",...}").build())
    .build();
FilesystemConfig fsGcs = FilesystemConfig.builder()
    .provider(FsProvider.GCS.getCode())
    .gcsconfig(gcs)
    .build();

// Azure Blob (provider = 3)
AzureBlobFsConfig az = AzureBlobFsConfig.builder()
    .container("container")
    .accountName("minha-conta")
    .accountKey(Secret.builder().status("Plain").payload("key").build())
    .build();
FilesystemConfig fsAz = FilesystemConfig.builder()
    .provider(FsProvider.AZURE_BLOB.getCode())
    .azblobconfig(az)
    .build();
```

Também existem: `OSFsConfig` (local), `CryptFsConfig` (criptografado), `SFTPFsConfig`, `HTTPFsConfig`. Use o enum `FsProvider` (LOCAL=0, S3=1, GCS=2, AZURE_BLOB=3, CRYPT=4, SFTP=5, HTTP=6) para o campo `provider`.

### Quota

```java
import balbucio.org.sftpgo.model.QuotaScan;
import balbucio.org.sftpgo.model.QuotaUsage;
import balbucio.org.sftpgo.model.FolderQuotaScan;
import java.util.List;

// Scans de quota (usuários)
List<QuotaScan> scans = client.quota().getUserQuotaScans();
client.quota().startUserQuotaScan("joao");

// Atualizar uso de quota (disco e transferência)
client.quota().updateUserQuotaUsage("joao", QuotaUsage.builder().usedQuotaSize(1024L).usedQuotaFiles(10).build());
client.quota().updateUserQuotaUsage("joao", usage, "reset"); // ou "add"

// Scans e uso para pastas
List<FolderQuotaScan> folderScans = client.quota().getFolderQuotaScans();
client.quota().startFolderQuotaScan("compartilhado");
client.quota().updateFolderQuotaUsage("compartilhado", usage);
```

### Erros

Em respostas 4xx/5xx a API lança `balbucio.org.sftpgo.client.ApiException`:

```java
import balbucio.org.sftpgo.client.ApiException;

try {
    client.users().getUserByUsername("inexistente");
} catch (ApiException e) {
    int status = e.getStatusCode();
    String body = e.getResponseBody();
}
```

## Referência da API

Documentação oficial: [SFTPGo REST API](https://sftpgo.com/rest-api).
