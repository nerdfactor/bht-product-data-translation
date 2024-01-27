describe('search page', () => {
  beforeEach(function () {
    cy.visit('http://localhost:4200/create', {
      onBeforeLoad: (win) => {
        Object.defineProperty(win.navigator, 'languages', {
          value: ['de-DE'],
        });
      }
    });
  });

  it('should navigate back', () => {
    cy.get('a[href="/search"]').click();
    cy.url().should('include', 'search');
  });

  it('should fill out all inputs', () => {
    cy.get('input[formcontrolname="name"]').type('Produkt');
    cy.get('input[formcontrolname="serial"]').type('123456');
    cy.get('input[formcontrolname="shortDescription"]').type('ein Produkt');
    cy.get('textarea[formcontrolname="longDescription"]').type('ein Produkt mit langer Beschreibung');
    cy.get('input[formcontrolname="weight"]').type('100');
    cy.get('input[formcontrolname="height"]').type('10');
    cy.get('input[formcontrolname="width"]').type('10');
    cy.get('input[formcontrolname="depth"]').type('10');
    cy.get('input[formcontrolname="price"]').type('99.99');
  });

  it('should pick option on focus', () => {
    cy.get('#mat-mdc-chip-list-input-0').focus()
      .get('mat-option[ng-reflect-value="Rot"]').click();
    cy.get('#mat-mdc-chip-list-input-1').focus()
      .get('mat-option[ng-reflect-value="Stuhl"]').click();
  });

  it('should pick options on typing', () => {
    cy.get('#mat-mdc-chip-list-input-0').focus().type('Ro')
      .get('mat-option[ng-reflect-value="Rot"]').click();
    cy.get('#mat-mdc-chip-list-input-1').focus().type('Stu')
      .get('mat-option[ng-reflect-value="Stuhl"]').click();
  });

  it('should submit required formular', () => {
    cy.get('input[formcontrolname="name"]').type('Produkt');
    cy.get('input[formcontrolname="serial"]').type('123456');
    cy.get('button[type="submit"').click();
    cy.get('div').contains('Gespeichert');
  });

  it('should select image', () => {
    cy.contains('label', 'Foto').invoke('attr', 'for').then(id =>
      cy.get('#' + id).selectFile({
      contents: Cypress.Buffer.from(''),
      fileName: 'image.jpg',
      mimeType: 'multipart/form-data'
    }, { force: true })
  )});

  it('should submit fully filled formular', () => {
    cy.get('input[formcontrolname="name"]').type('Produkt');
    cy.get('input[formcontrolname="serial"]').type('123456');
    cy.get('input[formcontrolname="shortDescription"]').type('ein Produkt');
    cy.get('textarea[formcontrolname="longDescription"]').type('ein Produkt mit langer Beschreibung');
    cy.get('input[formcontrolname="weight"]').type('100');
    cy.get('input[formcontrolname="height"]').type('10');
    cy.get('input[formcontrolname="width"]').type('10');
    cy.get('input[formcontrolname="depth"]').type('10');
    cy.get('input[formcontrolname="price"]').type('99.99');
    cy.get('#mat-mdc-chip-list-input-0').focus()
      .get('mat-option[ng-reflect-value="Rot"]').click();
    cy.get('#mat-mdc-chip-list-input-1').focus()
      .get('mat-option[ng-reflect-value="Stuhl"]').click();
    cy.get('button[type="submit"').click();
    cy.get('div').contains('Gespeichert');
  });

  it('should show warning if not german', () => {
    cy.get('button').contains('Deutsch').click().get('button').contains('English').click();
    cy.get('.alert-warning').contains('German');
  });
});
