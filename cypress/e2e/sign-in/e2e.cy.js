describe("e2e test", () => {
    before(() => {
        cy.visit("http://localhost:8088/auth/realms/CX-Central/protocol/openid-connect/auth?client_id=Cl13-CX-Battery&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2F&state=95c36428-3a44-42cb-954d-d3dd1687f6a2&response_mode=fragment&response_type=code&scope=openid&nonce=46198261-b6a4-4502-a00d-11f4cb5d3760");
    });
    it("Sign in and battery select", () => {
        cy.get('#username').type("company 2 user");
        cy.get('#password').type("changeme");
        cy.get('#kc-login').click();
        // cy.get('[data-cy="battery-pass-container"]');
        // cy.get('[data-cy="provider-select"]').select("BMW");
        // cy.get('[data-cy="battery-select"]').select("test-battery 1 (work)");
        // cy.get('[data-cy="passport-btn"]').click();
        //cy.get('[data-cy="select-battery"]').click();
    });
    // it("Read passport details", () => {
    //     cy.get('[data-cy="battery-id"]'); // 1. General information first field check
    //     cy.get('[data-cy="electrolyte-composition"]'); // 2. Battery composition first field check
    //     cy.get('[data-cy="remaining-capacity"]'); // 3. State of health first field check
    //     cy.get('[data-cy="state-of-charge"]'); // 4. Parameters of the battery first field check
    //     cy.get('[data-cy="dismantling-procedures"]'); // 5. Dismantling Procedures first field check
    //     cy.get('[data-cy="occupational-safety"]'); // 6. Safety Information of the battery first field check
    //     cy.get('[data-cy="responsible-raw-materials-report"]'); // 7. Information responsible sourcing first field check
    //     cy.get('[data-cy="link-to-the-label-element"]'); // 8. Additional information first field check
    //     cy.get('[data-cy="footer-logo-wrapper"]'); //  Footer check
    // });
});
