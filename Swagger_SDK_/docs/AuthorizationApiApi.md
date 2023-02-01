# AuthorizationApiApi

All URIs are relative to *http://d1dferkr9wdku7.cloudfront.net:80*

Method | HTTP request | Description
------------- | ------------- | -------------
[**loginUsingPOST**](AuthorizationApiApi.md#loginUsingPOST) | **POST** /authorization/login | Get token by login and password.

<a name="loginUsingPOST"></a>
# **loginUsingPOST**
> AuthResponse loginUsingPOST(username, password)

Get token by login and password.

Get a token to work with the API by login and password of the registered user

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.AuthorizationApiApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: Authorization
ApiKeyAuth Authorization = (ApiKeyAuth) defaultClient.getAuthentication("Authorization");
Authorization.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//Authorization.setApiKeyPrefix("Token");

AuthorizationApiApi apiInstance = new AuthorizationApiApi();
String username = "username_example"; // String | User username
String password = "password_example"; // String | User password
try {
    AuthResponse result = apiInstance.loginUsingPOST(username, password);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AuthorizationApiApi#loginUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **username** | **String**| User username |
 **password** | **String**| User password |

### Return type

[**AuthResponse**](AuthResponse.md)

### Authorization

[Authorization](../README.md#Authorization)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

