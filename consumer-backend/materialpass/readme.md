# ![Product Battery Passport Consumer Backend](../../docs/catena-x-logo.svg) Product Battery Passport Consumer Backend

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
\api\assets #This api returns the content of the assets dataModel
\api\assets\{id} # This api returns the ID, checking the success of the request
```
>  **_NOTE:_** You must be authenticated with the keycloak instance to access this apis

## License
[Apache-2.0](https://raw.githubusercontent.com/catenax-ng/product-battery-passport-consumer-app/main/LICENSE)
