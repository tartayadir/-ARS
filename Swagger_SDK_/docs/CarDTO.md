# CarDTO

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**additionalOptions** | **List&lt;String&gt;** | Additional options allow you to equip the selected package already needed by the owner list of features. |  [optional]
**brand** | [**BrandEnum**](#BrandEnum) | Car brand refers to the basic concept. Otherwise, can be called as a brand or trademark. |  [optional]
**carBodyTypes** | [**CarBodyTypesEnum**](#CarBodyTypesEnum) | It is an element of the car design that determines the size, type and capacity of the car to carry goods and passengers. |  [optional]
**engineCapacity** | **Double** | The engine volume is defined as the total volume of all cylinders, or the volume of one cylinder multiplied by their number. |  [optional]
**fullDescription** | **String** | It is the full description of car that contains some details about car. |  [optional]
**id** | **Long** | Car ID that is used for find car in data base. |  [optional]
**imageFileId** | **String** | Car image file name that you can find at AWS S# by it. |  [optional]
**model** | **String** | The model describes the type of vehicle (what type of body) produced under a certain brand. |  [optional]
**shortDescription** | **String** | It is the short description of car that describe car in general. |  [optional]
**transmissionBoxTypes** | [**TransmissionBoxTypesEnum**](#TransmissionBoxTypesEnum) | is a collection of different units and mechanisms that transmit torque from the engine to the driving wheels and change it in size and direction. |  [optional]
**year** | **Integer** | The year of issue is indicated by one digit, which corresponds to the last digit of the calendar year (similar to VIN). |  [optional]

<a name="BrandEnum"></a>
## Enum: BrandEnum
Name | Value
---- | -----
ACURA | &quot;ACURA&quot;
ALFA | &quot;ALFA&quot;
ASTON_MARTIN | &quot;ASTON_MARTIN&quot;
AUDI | &quot;AUDI&quot;
BENTLEY | &quot;BENTLEY&quot;
BMW | &quot;BMW&quot;
BRILLIANCE | &quot;BRILLIANCE&quot;
BUGATTI | &quot;BUGATTI&quot;
BUICK | &quot;BUICK&quot;
BYD | &quot;BYD&quot;
CADILLAC | &quot;CADILLAC&quot;
CHANGAN | &quot;CHANGAN&quot;
CHERY | &quot;CHERY&quot;
CHEVROLET | &quot;CHEVROLET&quot;
CHRYSLER | &quot;CHRYSLER&quot;
CITROEN | &quot;CITROEN&quot;
DACIA | &quot;DACIA&quot;
DAEWOO | &quot;DAEWOO&quot;
DAIHATSU | &quot;DAIHATSU&quot;
DATSUN | &quot;DATSUN&quot;
DODGE | &quot;DODGE&quot;
FAW | &quot;FAW&quot;
FERRARI | &quot;FERRARI&quot;
FIAT | &quot;FIAT&quot;
FORD | &quot;FORD&quot;
GEELY | &quot;GEELY&quot;
GENESIS | &quot;GENESIS&quot;
GMC | &quot;GMC&quot;
GREAT_WALL | &quot;GREAT_WALL&quot;
HAVAL_ROVER | &quot;HAVAL_ROVER&quot;
HONDA | &quot;HONDA&quot;
HUMMER | &quot;HUMMER&quot;
HYUNDAI | &quot;HYUNDAI&quot;
INFINITI | &quot;INFINITI&quot;
JAGUAR | &quot;JAGUAR&quot;
JEEP | &quot;JEEP&quot;
KIA | &quot;KIA&quot;
LAMBORGHINI | &quot;LAMBORGHINI&quot;
LANCIA | &quot;LANCIA&quot;
LAND_ROVER | &quot;LAND_ROVER&quot;
LEXUS | &quot;LEXUS&quot;
LIFAN | &quot;LIFAN&quot;
LINCOLN | &quot;LINCOLN&quot;
LOTUS | &quot;LOTUS&quot;
MARUSSIA | &quot;MARUSSIA&quot;
MASERATI | &quot;MASERATI&quot;
MAYBACH | &quot;MAYBACH&quot;
MAZDA | &quot;MAZDA&quot;
MCLAREN | &quot;MCLAREN&quot;
MERCEDES | &quot;MERCEDES&quot;
MINI | &quot;MINI&quot;
MITSUBISHI | &quot;MITSUBISHI&quot;
NISSAN | &quot;NISSAN&quot;
OPEL | &quot;OPEL&quot;
PEUGEOT | &quot;PEUGEOT&quot;
PONTIAC | &quot;PONTIAC&quot;
PORSCHE | &quot;PORSCHE&quot;
RENAULT | &quot;RENAULT&quot;
ROLLS_ROYCE | &quot;ROLLS_ROYCE&quot;
ROMEO | &quot;ROMEO&quot;
SAAB | &quot;SAAB&quot;
SEAT | &quot;SEAT&quot;
SKODA | &quot;SKODA&quot;
SMART | &quot;SMART&quot;
SSANGYONG | &quot;SSANGYONG&quot;
SUBARU | &quot;SUBARU&quot;
SUZUKI | &quot;SUZUKI&quot;
TAGAZ | &quot;TAGAZ&quot;
TESLA | &quot;TESLA&quot;
TOYOTA | &quot;TOYOTA&quot;
VOLKSWAGEN | &quot;VOLKSWAGEN&quot;
VOLVO | &quot;VOLVO&quot;

<a name="CarBodyTypesEnum"></a>
## Enum: CarBodyTypesEnum
Name | Value
---- | -----
CONVERTIBLE | &quot;CONVERTIBLE&quot;
COUPE | &quot;COUPE&quot;
HATCHBACK | &quot;HATCHBACK&quot;
MINIVAN | &quot;MINIVAN&quot;
PICKUP | &quot;PICKUP&quot;
SEDAN | &quot;SEDAN&quot;
SPORTS_CAR | &quot;SPORTS_CAR&quot;
STATION_WAGON | &quot;STATION_WAGON&quot;
SUV | &quot;SUV&quot;

<a name="TransmissionBoxTypesEnum"></a>
## Enum: TransmissionBoxTypesEnum
Name | Value
---- | -----
AUTOMATIC | &quot;AUTOMATIC&quot;
MECHANICAL | &quot;MECHANICAL&quot;
ROBOTIC | &quot;ROBOTIC&quot;
VARIATIONAL | &quot;VARIATIONAL&quot;
