describe('template spec', () => {
  it('loads page', () => {
    cy.visit('http://localhost:4200/');
    cy.url().should('include', '/search');
  });
});