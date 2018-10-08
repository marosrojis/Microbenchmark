import { Operation } from './operation.model';

export class Project {
  public id: string;
  public operation: Operation;

  constructor(id: string, operation: Operation) {
    this.id = id;
    this.operation = operation;
  }
}
