import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'find'
})
export class FindPipe implements PipeTransform {

  transform(values: any[] | null | undefined, ...args: any[]): any {
    let fieldNames = args[0].split('.');
    let fieldValue = args[1];
    return values?.find(value => fieldNames.reduce((a: any, c: string) => a[c], value) == fieldValue);
  }

}
