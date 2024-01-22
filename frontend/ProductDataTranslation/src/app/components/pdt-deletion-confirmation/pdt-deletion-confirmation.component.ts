import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'pdt-deletion-confirmation',
  templateUrl: './pdt-deletion-confirmation.component.html',
  styleUrl: './pdt-deletion-confirmation.component.scss'
})
export class PdtDeletionConfirmationComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: {name:string},
              private dialogRef: MatDialogRef<PdtDeletionConfirmationComponent>) {
  }

  confirm() {
    this.dialogRef.close(true);
  }

  deny() {
    this.dialogRef.close(false);
  }
}
