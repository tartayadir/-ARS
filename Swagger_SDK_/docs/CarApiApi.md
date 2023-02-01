# CarApiApi

All URIs are relative to *http://d1dferkr9wdku7.cloudfront.net:80*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addCarUsingPOST**](CarApiApi.md#addCarUsingPOST) | **POST** /car-catalog | Add car.
[**getAllCarsUsingGET**](CarApiApi.md#getAllCarsUsingGET) | **GET** /car-catalog | All cars.
[**getCarUsingGET**](CarApiApi.md#getCarUsingGET) | **GET** /car-catalog/{id} | Found car.
[**removeCarUsingDELETE**](CarApiApi.md#removeCarUsingDELETE) | **DELETE** /car-catalog/{id} | Delete car and their image.
[**updateCarUsingPUT**](CarApiApi.md#updateCarUsingPUT) | **PUT** /car-catalog | Update car.

<a name="addCarUsingPOST"></a>
# **addCarUsingPOST**
> CarDTO addCarUsingPOST(body)

Add car.

Makes the car and returns the car if all fields of the machine are valid. If not valid then return 400.

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.CarApiApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: Authorization
ApiKeyAuth Authorization = (ApiKeyAuth) defaultClient.getAuthentication("Authorization");
Authorization.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//Authorization.setApiKeyPrefix("Token");

CarApiApi apiInstance = new CarApiApi();
CarDTO body = new CarDTO(); // CarDTO | 
try {
    CarDTO result = apiInstance.addCarUsingPOST(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CarApiApi#addCarUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CarDTO**](CarDTO.md)|  | [optional]

### Return type

[**CarDTO**](CarDTO.md)

### Authorization

[Authorization](../README.md#Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getAllCarsUsingGET"></a>
# **getAllCarsUsingGET**
> List&lt;CarDTO&gt; getAllCarsUsingGET()

All cars.

Returns all owned сфкы.

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.CarApiApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: Authorization
ApiKeyAuth Authorization = (ApiKeyAuth) defaultClient.getAuthentication("Authorization");
Authorization.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//Authorization.setApiKeyPrefix("Token");

CarApiApi apiInstance = new CarApiApi();
try {
    List<CarDTO> result = apiInstance.getAllCarsUsingGET();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CarApiApi#getAllCarsUsingGET");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;CarDTO&gt;**](CarDTO.md)

### Authorization

[Authorization](../README.md#Authorization)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getCarUsingGET"></a>
# **getCarUsingGET**
> CarDTO getCarUsingGET(id)

Found car.

Returns the car by id if the car was found. If no such machine is found, it returns 404 or if no valid id returns 400.

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.CarApiApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: Authorization
ApiKeyAuth Authorization = (ApiKeyAuth) defaultClient.getAuthentication("Authorization");
Authorization.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//Authorization.setApiKeyPrefix("Token");

CarApiApi apiInstance = new CarApiApi();
Long id = 789L; // Long | id of car to return
try {
    CarDTO result = apiInstance.getCarUsingGET(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CarApiApi#getCarUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| id of car to return |

### Return type

[**CarDTO**](CarDTO.md)

### Authorization

[Authorization](../README.md#Authorization)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="removeCarUsingDELETE"></a>
# **removeCarUsingDELETE**
> CarDTO removeCarUsingDELETE(id, imageId)

Delete car and their image.

Removes machine and image by id. For valid response try integer IDs with positive integer value. Negative or non-integer values will generate API errors

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.CarApiApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: Authorization
ApiKeyAuth Authorization = (ApiKeyAuth) defaultClient.getAuthentication("Authorization");
Authorization.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//Authorization.setApiKeyPrefix("Token");

CarApiApi apiInstance = new CarApiApi();
Long id = 789L; // Long | ID of car that needs to be deleted.
String imageId = "imageId_example"; // String | name of mage file that has car needs to be deleted
try {
    CarDTO result = apiInstance.removeCarUsingDELETE(id, imageId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CarApiApi#removeCarUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| ID of car that needs to be deleted. |
 **imageId** | **String**| name of mage file that has car needs to be deleted |

### Return type

[**CarDTO**](CarDTO.md)

### Authorization

[Authorization](../README.md#Authorization)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="updateCarUsingPUT"></a>
# **updateCarUsingPUT**
> CarDTO updateCarUsingPUT(body)

Update car.

Finds the car by the id and updates it. If the fields are not valid then return 400 or if the cars were not found, return 404

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.CarApiApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: Authorization
ApiKeyAuth Authorization = (ApiKeyAuth) defaultClient.getAuthentication("Authorization");
Authorization.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//Authorization.setApiKeyPrefix("Token");

CarApiApi apiInstance = new CarApiApi();
CarDTO body = new CarDTO(); // CarDTO | 
try {
    CarDTO result = apiInstance.updateCarUsingPUT(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CarApiApi#updateCarUsingPUT");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CarDTO**](CarDTO.md)|  | [optional]

### Return type

[**CarDTO**](CarDTO.md)

### Authorization

[Authorization](../README.md#Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

