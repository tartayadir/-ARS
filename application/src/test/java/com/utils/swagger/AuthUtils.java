package com.utils.swagger;

import io.swagger.client.ApiClient;
import io.swagger.client.api.AuthorizationApiApi;
import lombok.SneakyThrows;

import static com.utils.spring.AuthTestUtils.getFirstAdminPassword;
import static com.utils.spring.AuthTestUtils.getFirstAdminUsername;

public class AuthUtils {

    private final static AuthorizationApiApi authorizationApiApi = new AuthorizationApiApi();

    private final static String TOKEN_PREFIX = "Bearer ";

    @SneakyThrows
    public static ApiClient getAuthorizedApiClient() {

        String token = authorizationApiApi.loginUsingPOST(getFirstAdminUsername(), getFirstAdminPassword()).
                getAccessToken();
        ApiClient apiClient = new ApiClient();
        apiClient.setApiKey(TOKEN_PREFIX + token);

        return apiClient;
    }
}
