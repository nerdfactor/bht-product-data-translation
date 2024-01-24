describe('edit page', () => {
  beforeEach(function () {
    cy.visit('http://localhost:4200/edit/1');
  });

  it('should navigate back', () => {
    cy.get('a[href="/product/1"]').click();
    cy.url().should('include', 'product/1');
  });

  it('should show filled out inputs', () => {
    cy.get('input[formcontrolname="name"]').invoke('val').should('contain', 'Massagestuhl');
    cy.get('input[formcontrolname="serial"]').should('have.value', 'W123456');
    cy.get('input[formcontrolname="shortDescription"]').invoke('val').should('contain', 'Massagestuhl');
    cy.get('textarea[formcontrolname="longDescription"]').invoke('val').should('contain', 'Machen Sie Wellness zu einem täglichen Ritual');
    cy.get('input[formcontrolname="weight"]').should('have.value', '12');
    cy.get('input[formcontrolname="height"]').should('have.value', '120');
    cy.get('input[formcontrolname="width"]').should('have.value', '60');
    cy.get('input[formcontrolname="depth"]').should('have.value', '80');
    cy.get('input[formcontrolname="price"]').should('have.value', '899');
    cy.get('div').contains('Rot');
    cy.get('div').contains('Stuhl');
  });

  it('should refill inputs', () => {
    cy.get('input[formcontrolname="name"]').clear().type('Massagestuhl 2');
    cy.get('input[formcontrolname="serial"]').clear().type('7890');
    cy.get('input[formcontrolname="shortDescription"]').clear().type('Massagestuhl der nächsten Generation');
    cy.get('textarea[formcontrolname="longDescription"]').clear().type('Eine neue lange Beschreibung des Massagestuhl 2');
    cy.get('input[formcontrolname="weight"]').clear().type('21');
    cy.get('input[formcontrolname="height"]').clear().type('210');
    cy.get('input[formcontrolname="width"]').clear().type('90');
    cy.get('input[formcontrolname="depth"]').clear().type('40');
    cy.get('input[formcontrolname="price"]').clear().type('1099');
    cy.get('button').contains('cancel').click();
    cy.get('button').contains('cancel').click();
    cy.get('#mat-mdc-chip-list-input-0').type('bl').get('[role="option"]').contains('Blau').click();
    cy.get('#mat-mdc-chip-list-input-1').type('stu', {force: true}).get('[role="option"]').contains('Stuhl').click();
  });

  it('should warn when deleting', () => {
    cy.get('button').contains('delete').click({force: true});
    cy.get('div').contains('Produkt löschen?');
  });

  it('should make title, serialnumber & properties readonly if not german', () => {
    cy.get('button').contains('Deutsch').click().get('button').contains('English').click();
    cy.get('input[formcontrolname="name"').should('not.exist');
    cy.get('h1').contains('massage chair');
    cy.get('input[formcontrolname="serial"').should('not.exist');
    cy.get('p').contains('serial number');
    cy.get('input[formcontrolname="weight"').should('not.exist');
    cy.get('p').contains('Weight');
    cy.get('input[formcontrolname="height"').should('not.exist');
    cy.get('p').contains('H, W, d');
    cy.get('input[formcontrolname="width"').should('not.exist');
    cy.get('p').contains('Price');
    cy.get('input[formcontrolname="depth"').should('not.exist');
    cy.get('p').contains('Colors');
    cy.get('input[formcontrolname="price"').should('not.exist');
    cy.get('p').contains('Categories');
    cy.get('#mat-mdc-chip-list-input-0').should('not.exist');
    cy.get('#mat-mdc-chip-list-input-1').should('not.exist');
    cy.get('input[formcontrolname="shortDescription"').should('exist');
    cy.get('textarea[formcontrolname="longDescription"').should('exist');
  });

  it('should pick an image', () => {
    cy.contains('label', 'Foto').invoke('attr', 'for').then(id => 
      cy.get('#' + id).selectFile({
      contents: Cypress.Buffer.from(''),
      fileName: 'image.jpg',
      mimeType: 'multipart/form-data'
    }, { force: true })
  )});

  it('should save changes', () => {
    cy.get('input[formcontrolname="name"').type('test');
    cy.get('form').submit();
    cy.get('div').contains('Gespeichert');
    cy.get('input[formcontrolname="name"').clear().type('Luxuriöser Wellness-Massagestuhl');
    cy.get('form').submit();
    cy.get('div').contains('Gespeichert');
  })
});