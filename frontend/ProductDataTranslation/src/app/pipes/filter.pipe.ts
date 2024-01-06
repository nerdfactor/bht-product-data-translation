import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {

  transform(values: any[], ...args: any[]): any[] {
    let fieldNames = args[0].split('.');
    let fieldValue = args[1];
    return values.filter(value => fieldNames.reduce((a: any, c: string) => a[c], value) == fieldValue);
  }
}
