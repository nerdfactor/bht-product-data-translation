describe('search page', () => {
  beforeEach(function () {
    cy.visit('http://localhost:4200/', {
      onBeforeLoad: (win) => {
        Object.defineProperty(win.navigator, 'languages', {
          value: ['de-DE'],
        });
      }
    });
  })

  it('should move to search page', () => {
    cy.url().should('include', '/search');
  });

  it('should filter for Massagestuhl', () => {
    cy.get('input[type="text"]').type('Massage');
    cy.get('td').contains('Massagestuhl');
  });

  it('should paginate to next page', () => {
    cy.get('button[aria-label="Nächste Seite"]').click();
    cy.get('td').contains('Reiserucksack');
  });

  it('should change language', () => {
    cy.get('button').contains('Deutsch').click()
      .get('button').contains('English').click();
    cy.get('label').contains('Seek');
    cy.get('td').contains('massage chair');
  });

  it('should open create new produkt', () => {
    cy.get('a[href="/create"]').click();
    cy.url().should('include', 'create');
  });

  it('should open product details', () => {
    cy.get('a[href="/product/1"]').click();
    cy.url().should('include', 'product/1');
  });
});
