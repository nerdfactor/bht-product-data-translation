import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { MatChipEditedEvent, MatChipInputEvent } from '@angular/material/chips';
import { Observable, map, startWith } from 'rxjs';

@Component({
  selector: 'pdt-chooser',
  templateUrl: './pdt-chooser.component.html',
  styleUrl: './pdt-chooser.component.scss'
})
export class PdtChooserComponent {
  @Input()
  label?: string | null;

  @Input()
  options?: Array<any> | null;
  
  @Input()
  set?: Array<any> | null;

  @Output()
  setChange = new EventEmitter<Array<any>>();

  @ViewChild('itemInput')
  itemInput!: ElementRef<HTMLInputElement>;


  addOnBlur = false;
  readonly separatorKeyCodes = [ENTER, COMMA] as const;
  itemControl = new FormControl('');
  filteredOptions$!: Observable<any[]>;

  ngOnChanges() {
    if (this.set == null)
      return;

    this.filteredOptions$ = this.itemControl.valueChanges.pipe(
      startWith(''),
      map(value => this.filter(value ?? ''))
    );
  }

  private filter(value: string): any[] {
    const filterValue = value.toLowerCase();

    return this.options?.filter(option => !this.set?.find(item => item.name == option.name) && option.name.toLowerCase().startsWith(filterValue)) ?? [];
  }

  selected(event: MatAutocompleteSelectedEvent) {
    this.addItem(event.option.value);
    this.itemInput.nativeElement.value = '';
  }

  add(event: MatChipInputEvent) {
    const value = (event.value || '').trim();
    if (value) {
      this.addItem(value)
    }

    event.chipInput.clear();
    this.itemControl.setValue('');
  }

  addItem(item: string) {
    if (!this.set)
      return;

    const selected = this.options?.find(option => option.name.toLowerCase() == item.toLowerCase());
    if (selected)
      this.set?.push(selected);
    else
      this.set?.push({name: item});
    this.setChange.emit(this.set);
  }

  remove(item: any) {
    const index = this.set?.indexOf(item);

    if (index != null && index >= 0) {
      this.set?.splice(index, 1);
    }

    if (this.set)
      this.setChange.emit(this.set);
  }

  edit(item: any, event: MatChipEditedEvent) {
    const value = event.value.trim();
    if (!value) {
      this.remove(item);
      return;
    }

    if (this.set != null)  {
      const index = this.set.indexOf(item);
      if (index != null && index >= 0) {
        this.set[index].name = value;
      }
      this.setChange.emit(this.set);
    }
  }
}
