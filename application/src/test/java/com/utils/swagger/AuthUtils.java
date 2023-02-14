package com.utils.swagger;

import com.implemica.swagger.client.codegen.rest.api.AuthorizationApiApi;
import com.implemica.swagger.client.codegen.rest.invoker.ApiClient;
import com.implemica.swagger.client.codegen.rest.model.AuthRequest;
import lombok.SneakyThrows;

import static com.utils.spring.AuthTestUtils.getFirstAdminPassword;
import static com.utils.spring.AuthTestUtils.getFirstAdminUsername;

public class AuthUtils {

    private final static AuthorizationApiApi authorizationApiApi = new AuthorizationApiApi();

    private final static String TOKEN_PREFIX = "Bearer ";

    @SneakyThrows
    public static ApiClient getAuthorizedApiClient() {

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername(getFirstAdminUsername());
        authRequest.setPassword(getFirstAdminPassword());

        String token = authorizationApiApi.loginUsingPOST(authRequest).
                getAccessToken();
        ApiClient apiClient = new ApiClient();
        apiClient.setApiKey(TOKEN_PREFIX + token);

        return apiClient;
    }
}
