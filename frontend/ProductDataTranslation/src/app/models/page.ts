export class Page<T> {
  content: T[] = [];
  pageable: Pageable = new Pageable();
  sort: Sort = new Sort();
  totalPages: number = 0;
  totalElements: number = 0;
  last: boolean = false;
  first: boolean = false;
  size: number = 0;
  number: number = 0;
  numberOfElements: number = 0;
  empty: boolean = false;
}

export class Pageable {
  pageNumber: number = 0;
  pageSize: number = 0;
  sort: Sort = new Sort();
  offset: number = 0;
  paged: boolean = false;
  unpaged: boolean = false;
}

export class Sort {
  sorted: boolean = false;
  unsorted: boolean = true;
  empty: boolean = true;
}
