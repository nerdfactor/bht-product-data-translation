import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'pdt-deletion-confirmation',
  templateUrl: './pdt-deletion-confirmation.component.html',
  styleUrl: './pdt-deletion-confirmation.component.scss'
})
export class PdtDeletionConfirmationComponent {
  constructor(private dialogRef: MatDialogRef<PdtDeletionConfirmationComponent>) {}

  confirm() {
    this.dialogRef.close(true);
  }

  deny() {
    this.dialogRef.close(false);
  }
}
