/* eslint-disable no-undef */
describe("e2e test", () => {
  before(() => {
    cy.visit("http://localhost:8080");
  });
  it("Sign in and battery select", () => {
    cy.get('#username').type("company 2 user");
    cy.get('#password').type("changeme");
    cy.get('#kc-login').click();
    cy.get('[data-cy="QR-scanner-tab"]').click();
    cy.get('[data-cy="qr-container"]');
    cy.get('[data-cy="history-tab"]').click();
    cy.get('[data-cy="history-container"]');
    cy.get('[data-cy="passport-link"]').click();
    cy.wait(20000);
    cy.get('[data-cy="battery-id"]'); // 1. General information first field check
    cy.get('[data-cy="electrolyte-composition"]'); // 2. Battery composition first field check
    cy.get('[data-cy="remaining-capacity"]'); // 3. State of health first field check
    cy.get('[data-cy="state-of-charge"]'); // 4. Parameters of the battery first field check
    cy.get('[data-cy="link-to-the-label-element"]'); // 8. Additional information first field check
    cy.get('[data-cy="footer"]'); //  Footer check
  });
});
