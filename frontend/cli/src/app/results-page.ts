export interface ResultsPage<T> {
  content: T[];
  number: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}
