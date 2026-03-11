# sftpgo-java

SDK em Java 11 para a [API REST do SFTPGo](https://sftpgo.com/rest-api). Inclui as categorias **Users**, **Groups**, **Quota** e **Folder** (virtual folders).

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
