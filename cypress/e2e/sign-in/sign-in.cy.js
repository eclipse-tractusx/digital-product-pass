// eslint-disable-next-line no-undef
describe("SignIn component tests", () => {
  // eslint-disable-next-line no-undef
  beforeEach(() => {
    // eslint-disable-next-line no-undef
    cy.visit("http://localhost:8080");
  });
  // eslint-disable-next-line no-undef
  it("check sign-in-container", () => {
    // eslint-disable-next-line no-undef
    cy.get('[data-cy="sign-in-container"]');
  });
});
