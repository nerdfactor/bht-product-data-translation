/**
 * Page is a wrapper for a list of generic items with paging and sorting.
 */
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

/**
 * Paging parameters for a page.
 */
export class Pageable {
  pageNumber: number = 0;
  pageSize: number = 0;
  sort: Sort = new Sort();
  offset: number = 0;
  paged: boolean = false;
  unpaged: boolean = false;
}

/**
 * Sort parameters for a page.
 */
export class Sort {
  sorted: boolean = false;
  unsorted: boolean = true;
  empty: boolean = true;
}
