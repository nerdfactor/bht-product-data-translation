describe('details page', () => {
  beforeEach(function () {
    cy.visit('http://localhost:4200/product/1', {
      onBeforeLoad: (win) => {
        Object.defineProperty(win.navigator, 'languages', {
          value: ['de-DE'],
        });
      }
    });
  })

  it('should navigate back to search page', () => {
    cy.get('a[href="/search"]').click();
    cy.url().should('include', '/search');
  });

  it('should have title, serialnumber, long description and properties', () => {
    cy.get('h1').contains('Massagestuhl');
    cy.get('p').contains('W123456');
    cy.get('div').contains('Machen Sie Wellness zu einem täglichen Ritual');
    cy.get('p').contains('Gewicht');
    cy.get('p').contains('H, B, T');
    cy.get('p').contains('Preis');
    cy.get('p').contains('Farben');
    cy.get('p').contains('Kategorien');
  });

  it('should be editable', () => {
    cy.get('a[href="/edit/1"]').click();
    cy.url().should('include', 'edit/1');
  });

  it('should not have image', () => {
    cy.get('img').should('not.exist').then(() => {
      cy.get('[role="img"]').contains('hide_image');
    });
  });

  it('should translate to danish', () => {
    cy.get('button').contains('Deutsch').click().get('button').contains('Dansk').click();
    cy.get('h1').contains('massagestol');
  });
});
