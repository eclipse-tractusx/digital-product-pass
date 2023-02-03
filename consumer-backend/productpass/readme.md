<!--
 Copyright 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
     http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->

# ![Product Battery Passport Consumer Backend](../../docs/catena-x-logo.svg) Product Battery Passport Consumer Backend

# Version: 0.0.5

## What is this backend app responsible for?

This backend includes the services and *logics* to manage the *passports* of the `frontend app`.

## Services Available

### Authentication Services

For login and log out!
```bash
\auth\login
\auth\check #With this api you can check you authentication status.
\auth\logout
\auth #Calling this api will redirect you to \auth\login
```

For Roles Authenticated API's!

```
\auth\oem
\auth\recycler
```

### API Services
At the moment api services are just implemented for testing.
```bash
\api\contracts #Get all the contracts available from the provider
\api\contracts\{assetId} #Get the contract for an assetId
```
>  **_NOTE:_** You must be authenticated with the keycloak instance to access this APIs

#### Contracts
You can search for contracts using the assetId from the product
![img.png](docs/media/img.png)

You can also get all the contracts available:
![img.png](docs/media/img2.png)
### Public APIs

Public APIs don't require authentication
```bash
\health #Get the health status of the server
```
```json
{
    "message": "RUNNING",
    "status": 200,
    "statusText": "Success",
    "data": "24/11/2022 17:48:18.487"
}
```

## License
[Apache-2.0](https://raw.githubusercontent.com/catenax-ng/product-battery-passport-consumer-app/main/LICENSE)
