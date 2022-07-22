describe("SignIn component tests", () => {
  beforeEach(() => {
    cy.visit("http://localhost:8080");
  });
  it("check sign-in-container", () => {
    cy.get('[data-cy="sign-in-container"]');
  });
});
