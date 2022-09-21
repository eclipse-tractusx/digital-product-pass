
Cypress.config()
// eslint-disable-next-line no-undef
describe("SignIn component tests", () => {
    // eslint-disable-next-line no-undef
    beforeEach(() => {
        // eslint-disable-next-line no-undef
        cy.visit("http://localhost:8088/auth/realms/CX-Central/protocol/openid-connect/auth?client_id=Cl13-CX-Battery&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2F&state=2322b639-d693-4e4d-84a8-f22436206391&response_mode=fragment&response_type=code&scope=openid&nonce=2728887f-7eda-4a49-b240-482848fe2b2f");
    });
    // eslint-disable-next-line no-undef
    it("sign-in", () => {
        // eslint-disable-next-line no-undef
        cy.get('#navbar').type('Hello, World');
    });
});
