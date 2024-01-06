import { FilterPipe } from './filter.pipe';

describe('FilterbyPipe', () => {
  it('create an instance', () => {
    const pipe = new FilterPipe();
    expect(pipe).toBeTruthy();
  });
});
