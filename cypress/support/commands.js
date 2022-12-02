// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add('login', (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add('drag', { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add('dismiss', { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite('visit', (originalFn, url, options) => { ... })
import 'cypress-keycloak';

// eslint-disable-next-line no-undef
Cypress.Commands.overwrite('login', (originalFn) => {
  originalFn({
    root: 'http://localhost:8088',
    realm: 'CX-Central',
    username: '',
    password: '',
    client_id: 'Cl13-CX-Battery',
    redirect_uri: 'http%3A%2F%2Flocalhost%3A8080%2F&state=95c36428-3a44-42cb-954d-d3dd1687f6a2&response_mode=fragment&response_type=code&scope=openid&nonce=46198261-b6a4-4502-a00d-11f4cb5d3760',
  });
});
