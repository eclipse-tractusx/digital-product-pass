/* eslint-disable no-undef */
describe("e2e test", () => {
  before(() => {
    cy.visit("http://localhost:8080");
  });
  it("Sign in and battery select", () => {
    cy.get('#username').type("company 2 user");
    cy.get('#password').type("changeme");
    cy.get('#kc-login').click();
    cy.get('[data-cy="dashboard-container"]');
    cy.get('[data-cy="search-btn"]').click();
    cy.get('[data-cy="search-input"]').type("tesla");
    cy.get('[data-cy="select-battery"]').click();
    cy.wait(20000);
    cy.get('[data-cy="battery-id"]'); // 1. General information first field check
    cy.get('[data-cy="electrolyte-composition"]'); // 2. Battery composition first field check
    cy.get('[data-cy="remaining-capacity"]'); // 3. State of health first field check
    cy.get('[data-cy="state-of-charge"]'); // 4. Parameters of the battery first field check
    cy.get('[data-cy="dismantling-procedures"]'); // 5. Dismantling Procedures first field check
    cy.get('[data-cy="occupational-safety"]'); // 6. Safety Information of the battery first field check
    cy.get('[data-cy="responsible-raw-materials-report"]'); // 7. Information responsible sourcing first field check
    cy.get('[data-cy="link-to-the-label-element"]'); // 8. Additional information first field check
    cy.get('[data-cy="footer-logo-wrapper"]'); //  Footer check
  });
});
