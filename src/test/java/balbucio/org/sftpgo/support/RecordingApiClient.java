package balbucio.org.sftpgo.support;

import balbucio.org.sftpgo.client.ApiClient;
import balbucio.org.sftpgo.model.ApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;

public class RecordingApiClient extends ApiClient {

	private Object nextResponse;
	private RuntimeException nextException;
	private String lastMethod;
	private String lastPath;
	private Object lastBody;
	private Class<?> lastResponseType;
	private TypeReference<?> lastTypeReference;

	public RecordingApiClient() {
		super("http://localhost:8080/api/v2", () -> null);
	}

	public void setNextResponse(Object nextResponse) {
		this.nextResponse = nextResponse;
	}

	public void setNextException(RuntimeException nextException) {
		this.nextException = nextException;
	}

	public String getLastMethod() {
		return lastMethod;
	}

	public String getLastPath() {
		return lastPath;
	}

	public Object getLastBody() {
		return lastBody;
	}

	public Class<?> getLastResponseType() {
		return lastResponseType;
	}

	public TypeReference<?> getLastTypeReference() {
		return lastTypeReference;
	}

	@Override
	public <T> T get(String path, Class<T> responseType) {
		record("GET", path, null);
		lastResponseType = responseType;
		throwIfNeeded();
		return responseType.cast(nextResponse);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(String path, TypeReference<T> typeRef) {
		record("GET", path, null);
		lastTypeReference = typeRef;
		throwIfNeeded();
		return (T) nextResponse;
	}

	@Override
	public <T> T post(String path, Object body, Class<T> responseType) {
		record("POST", path, body);
		lastResponseType = responseType;
		throwIfNeeded();
		return responseType.cast(nextResponse);
	}

	@Override
	public <T> T put(String path, Object body, Class<T> responseType) {
		record("PUT", path, body);
		lastResponseType = responseType;
		throwIfNeeded();
		return responseType.cast(nextResponse);
	}

	@Override
	public ApiResponse delete(String path) {
		record("DELETE", path, null);
		throwIfNeeded();
		return (ApiResponse) nextResponse;
	}

	private void record(String method, String path, Object body) {
		this.lastMethod = method;
		this.lastPath = path;
		this.lastBody = body;
	}

	private void throwIfNeeded() {
		if (nextException != null) {
			throw nextException;
		}
	}
}

