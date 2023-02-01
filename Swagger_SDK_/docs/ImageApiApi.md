# ImageApiApi

All URIs are relative to *http://d1dferkr9wdku7.cloudfront.net:80*

Method | HTTP request | Description
------------- | ------------- | -------------
[**deleteFileUsingDELETE**](ImageApiApi.md#deleteFileUsingDELETE) | **DELETE** /image/{imageId} | Delete image.
[**uploadFileUsingPOST**](ImageApiApi.md#uploadFileUsingPOST) | **POST** /image/{imageId} | Upload image.

<a name="deleteFileUsingDELETE"></a>
# **deleteFileUsingDELETE**
> deleteFileUsingDELETE(imageFileId)

Delete image.

Deletes car image by their name if image is not found returns 404 http status.

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ImageApiApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: Authorization
ApiKeyAuth Authorization = (ApiKeyAuth) defaultClient.getAuthentication("Authorization");
Authorization.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//Authorization.setApiKeyPrefix("Token");

ImageApiApi apiInstance = new ImageApiApi();
String imageFileId = "imageFileId_example"; // String | imageId
try {
    apiInstance.deleteFileUsingDELETE(imageFileId);
} catch (ApiException e) {
    System.err.println("Exception when calling ImageApiApi#deleteFileUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **imageFileId** | **String**| imageId |

### Return type

null (empty response body)

### Authorization

[Authorization](../README.md#Authorization)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="uploadFileUsingPOST"></a>
# **uploadFileUsingPOST**
> uploadFileUsingPOST(imageFileNameThatHasCarAndGetFromAWSS3ByItsImageFileId, imageFile)

Upload image.

Loads the image to AWS S3 and set image id to this file, if not a graft extension, returns 400 http status.

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ImageApiApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: Authorization
ApiKeyAuth Authorization = (ApiKeyAuth) defaultClient.getAuthentication("Authorization");
Authorization.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//Authorization.setApiKeyPrefix("Token");

ImageApiApi apiInstance = new ImageApiApi();
String imageFileNameThatHasCarAndGetFromAWSS3ByItsImageFileId = "imageFileNameThatHasCarAndGetFromAWSS3ByItsImageFileId_example"; // String | imageId
File imageFile = new File("imageFile_example"); // File | 
try {
    apiInstance.uploadFileUsingPOST(imageFileNameThatHasCarAndGetFromAWSS3ByItsImageFileId, imageFile);
} catch (ApiException e) {
    System.err.println("Exception when calling ImageApiApi#uploadFileUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **imageFileNameThatHasCarAndGetFromAWSS3ByItsImageFileId** | **String**| imageId |
 **imageFile** | **File**|  | [optional]

### Return type

null (empty response body)

### Authorization

[Authorization](../README.md#Authorization)

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: Not defined

